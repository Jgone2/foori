import { Injectable } from '@nestjs/common';
import { UploadService } from '../upload/upload.service';
import { ImageFolderEnum } from './dto/image-folder.enum';

@Injectable()
export class ImagesService {
  constructor(private readonly uploadService: UploadService) {}

  /**
   * 프로필 이미지 업로드 함수
   * @param file
   */
  async uploadUserProfileImage(id: string, file: Express.Multer.File) {
    const { userId, key, fileUrl } = await this.uploadService.uploadToS3(
      id,
      file,
      ImageFolderEnum.PROFILE,
    );

    return { userId, key, fileUrl };
  }
}
