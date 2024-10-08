import { HttpException, HttpStatus, Injectable } from '@nestjs/common';
import { MailerService } from '@nestjs-modules/mailer';
import { CacheService } from '../cache/cache.service';
import { ConfigService } from '@nestjs/config';

@Injectable()
export class MailService {
  constructor(
    private readonly mailerService: MailerService,
    private readonly cacheService: CacheService,
    private readonly configService: ConfigService,
  ) {
    console.log('Connecting to Redis:', {
      host: process.env.REDIS_HOST,
      port: process.env.REDIS_PORT,
      password: process.env.REDIS_PASSWORD,
      db: process.env.REDIS_DB,
    });
  }

  // 인증 이메일 보내기
  async sendVerificationEmail(to: string) {
    const verificationCode = await this.generateVerificationTokenCode();
    await this.verifyGenerateTokenCode(verificationCode);
    //ConfigService를 사용해 환경 변수 불러오기
    const expiresIn = this.configService.get<number>('REDIS_MAIL_EXPIRES_IN');

    // Redis에 인증 코드 저장
    await this.cacheService.redisSet(
      `verification_code:${to}`,
      verificationCode,
      expiresIn,
    );

    try {
      await this.mailerService.sendMail({
        to,
        subject: 'Foori 이메일 인증 요청',
        template: './verification',
        context: {
          verificationCode,
        },
      });
      console.log(`Verification code for ${to}: ${verificationCode}`);
    } catch (error) {
      console.error('Failed to send verification email', error);
    }
  }

  async generateVerificationTokenCode(): Promise<string> {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'; // 대문자와 숫자를 포함한 문자열
    let result = '';
    for (let i = 0; i < 6; i++) {
      result += chars.charAt(Math.floor(Math.random() * chars.length)); // 무작위로 한 글자 선택
    }
    return result;
  }

  async verifyGenerateTokenCode(code: string) {
    const regex = /^[A-Z0-9]{6}$/; // 6자리 대문자 또는 숫자로만 구성된지 확인하는 정규표현식

    if (regex.test(code)) {
      console.log('코드가 유효합니다:', code);
    } else {
      console.log('코드가 유효하지 않습니다:', code);
    }
  }

  async verifyCode(to: string, inputCode: string): Promise<number> {
    try {
      const storedCode = await this.cacheService.redisGet(
        `verification_code:${to}`,
      );
      console.log('Stored code:', storedCode);

      if (!storedCode) {
        console.log('인증번호가 만료되었거나 존재하지 않습니다.');
        throw new HttpException(
          '인증번호가 만료되었거나 존재하지 않습니다.',
          HttpStatus.BAD_REQUEST,
        );
      }

      if (storedCode === inputCode) {
        console.log('인증번호가 일치합니다.');
        await this.cacheService.redisDel(`verification_code:${to}`);
        return 1;
      } else {
        console.log('인증번호가 일치하지 않습니다.');
        throw new HttpException(
          '인증번호가 일치하지 않습니다.',
          HttpStatus.UNAUTHORIZED,
        );
      }
    } catch (error) {
      console.error('Verification code check failed', error);
      throw new HttpException(
        '인증번호 검증 실패',
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }
}
