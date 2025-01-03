import { BadRequestException, Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { UsersEntity } from './entities/users.entity';
import { Repository } from 'typeorm';
import { RegisterUserRequestDto } from './dto/register-user-request.dto';
import { FindUserPasswordRequestDto } from './dto/find-user-password-request.dto';
import { FindUserEmailRequestDto } from './dto/find-user-email-request.dto';
import * as bcrypt from 'bcryptjs';
import { UpdateUserRequestDto } from './dto/update-user-request.dto';
import { UpdateUserPasswordRequestDto } from './dto/update-user-password-request.dto';
import { ConfigService } from '@nestjs/config';
import { ImagesService } from '../common/images/images.service';
import { UploadService } from '../common/upload/upload.service';
import { UserLogsService } from '../user-logs/user-logs.service';

@Injectable()
export class UsersService {
  constructor(
    @InjectRepository(UsersEntity)
    private readonly usersRepository: Repository<UsersEntity>,
    private readonly imageService: ImagesService,
    private readonly uploadService: UploadService,
    private readonly userLogsService: UserLogsService,
    private readonly configService: ConfigService,
  ) {}

  /**
   * 회원 가입 함수
   * @param user
   */
  async createUser(user: RegisterUserRequestDto) {
    await this.verifyNonExistUserInfo(user);

    const profileImageUri = await this.createBasicProfileImage(user.name);
    const createdUser = this.usersRepository.create({
      ...user,
      profileImageUri,
    });

    return await this.usersRepository.save(createdUser);
  }

  /**
   * 회원 이메일 찾기 함수
   * @param user
   */
  async findUserEmail(user: FindUserEmailRequestDto) {
    try {
      const findUser = await this.usersRepository.findOneOrFail({
        where: {
          name: user.name,
          phoneNumber: user.phoneNumber,
        },
      });

      return findUser.email;
    } catch (error) {
      throw new BadRequestException('일치하는 정보가 없습니다.');
    }
  }

  /**
   * 회원 비밀번호 찾기 함수
   * @param user
   */
  async findUserPassword(user: FindUserPasswordRequestDto) {
    try {
      const findUser = await this.usersRepository.findOne({
        where: {
          email: user.email,
          name: user.name,
          phoneNumber: user.phoneNumber,
        },
      });

      return findUser.password;
    } catch (error) {
      throw new BadRequestException('일치하는 정보가 없습니다.');
    }
  }

  /**
   * 이메일로 회원을 찾는 함수
   * @param email
   */
  async findUserByEmail(email: string) {
    try {
      const findUser = await this.usersRepository.findOneOrFail({
        where: {
          email,
        },
      });

      return findUser;
    } catch (error) {
      throw new BadRequestException('일치하는 정보가 없습니다.');
    }
  }

  /**
   * ID로 회원을 찾는 함수
   * @param id
   */
  async findUserById(id: number) {
    try {
      const findUser = await this.usersRepository.findOneOrFail({
        where: {
          id,
        },
      });

      return findUser;
    } catch (error) {
      throw new BadRequestException('일치하는 정보가 없습니다.');
    }
  }

  /**
   * 메인페이지 회원 정보 조회 함수
   * @param userEmail
   */
  async getUserProfile(userEmail: string) {
    const findUser = await this.findUserByEmail(userEmail);

    return {
      id: findUser.id,
      name: findUser.name,
      profileImageUri: findUser.profileImageUri,
    };
  }

  /**
   * 회원 정보 수정 함수
   * @param userEmail
   * @param user
   */
  async updateUser(userEmail: string, user: UpdateUserRequestDto) {
    const findUser = await this.findUserByEmail(userEmail);

    // 객체 병합 없이 바로 save를 호출할 수 있음
    Object.assign(findUser, user); // 기존 유저 정보에 DTO로 받은 정보 병합

    return await this.usersRepository.save(findUser);
  }

  /**
   * 회원 비밀번호 수정 함수
   * @param userEmail
   * @param user
   */
  async updateUserPassword(
    userEmail: string,
    user: UpdateUserPasswordRequestDto,
  ) {
    const { currentPassword, newPassword } = user;

    const findUser = await this.findUserByEmail(userEmail);

    await this.verifyPassword(findUser.email, currentPassword);

    const hashRound = parseInt(
      this.configService.get<string>('HASH_ROUND'),
      10,
    );

    await findUser.setPassword(newPassword, hashRound);

    await this.userLogsService.updateUserLogsByChangePassword(userEmail);

    return await this.usersRepository.save(findUser);
  }

  /**
   * 프로필 이미지 업로드 함수
   * @param userEmail
   * @param file
   */
  async uploadUserProfileImage(userEmail: string, file: Express.Multer.File) {
    const findUser = await this.findUserByEmail(userEmail);

    const profileImageInfo = await this.imageService.uploadUserProfileImage(
      findUser.id.toString(),
      file,
    );

    Object.assign(findUser, {
      profileImageUri: profileImageInfo.fileUrl,
      profileImageKey: profileImageInfo.key,
    });

    return await this.usersRepository.save(findUser);
  }

  /**
   * 기본 프로필 이미지로 변경하는 함수
   * @param userEmail
   */
  async setBasicProfileImage(userEmail: string) {
    const findUser = await this.findUserByEmail(userEmail);

    const profileImageUri = await this.createBasicProfileImage(findUser.name);

    await this.uploadService.deleteFromS3(findUser.profileImageKey);

    Object.assign(findUser, {
      profileImageUri,
      profileImageKey: null,
    });

    return await this.usersRepository.save(findUser);
  }

  /**
   * 회원 가입 시 중복된 정보를 확인하는 함수
   * @param user
   */
  async verifyNonExistUserInfo(user: RegisterUserRequestDto) {
    const [emailExists, phoneNumberExists] = await Promise.all([
      this.usersRepository.exists({ where: { email: user.email } }),
      this.usersRepository.exists({ where: { phoneNumber: user.phoneNumber } }),
    ]);

    const errors = [];
    if (emailExists) errors.push('이미 존재하는 이메일입니다.');
    if (phoneNumberExists) errors.push('이미 존재하는 전화번호입니다.');

    if (errors.length > 0) {
      throw new BadRequestException(errors.join(', '));
    }
  }

  /**
   * 기본 프로필 이미지를 생성하는 함수
   * @param username
   */
  async createBasicProfileImage(username: string) {
    return `https://api.dicebear.com/9.x/notionists-neutral/svg?seed=${username}`;
  }

  /**
   * 비밀번호 확인 함수
   */
  async verifyPassword(userEmail: string, password: string) {
    const findUser = await this.findUserByEmail(userEmail);

    const passwdOk = await bcrypt.compare(password, findUser.password);

    if (!passwdOk) {
      throw new BadRequestException('비밀번호가 일치하지 않습니다.');
    }

    return 1;
  }

  /**
   * 회원 탈퇴 함수
   * @param userEmail
   */
  async withDrawUser(userEmail: string) {
    const findUser = await this.findUserByEmail(userEmail);

    Object.assign(findUser, { status: 9 });

    return await this.usersRepository.save(findUser);
  }

  /**
   * 회원 삭제 함수
   * @param userEmail
   */
  async deleteUser(userEmail: string) {
    const findUser = await this.findUserByEmail(userEmail);

    return await this.usersRepository.remove(findUser);
  }

  /**
   * 휴면 계정 전환 함수
   * @param userId
   */
  async updateUserStatusToDormant(userId: number) {
    const findUser = await this.findUserById(userId);

    Object.assign(findUser, { status: 4 });

    return await this.usersRepository.save(findUser);
  }

  /**
   * 비밀번호 변경 알림 함수
   */
  async sendPasswordChangeNotification(userId: number) {
    const findUser = await this.findUserById(userId);

    // 비밀번호 변경 알림 메일 발송
    console.log(`${findUser.email}님의 비밀번호 변경 알림 메일 발송`);

    return {
      email: findUser.email,
      name: findUser.name,
      message: '비밀번호 변경 알림 메일 발송 완료',
    };
  }

  /**
   * 회원 리스트 조회 함수(테스트용)
   */
  // async findAllUsers() {
  //   return await this.usersRepository.find();
  // }
}
