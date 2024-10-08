import {
  Body,
  Controller,
  Post,
  Headers,
  UseGuards,
  HttpStatus,
  HttpCode,
  Get,
} from '@nestjs/common';
import { AuthService } from './auth.service';
import { RegisterUserRequestDto } from '../users/dto/register-user-request.dto';
import { LoginUserRequestDto } from './dto/login-user-request.dto';
import { RefreshTokenGuard } from './guard/bearer-token.guard';
import { AuthKakaoService } from './auth-kakao.service';

@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('register')
  register(@Body() registerUserDto: RegisterUserRequestDto) {
    return this.authService.register(registerUserDto);
  }

  @Post('login')
  @HttpCode(HttpStatus.CREATED)
  login(@Body() loginUserDto: LoginUserRequestDto) {
    return this.authService.loginWithEmail(loginUserDto);
  }

  @Get('login/naver')
  @Post('token/access')
  @UseGuards(RefreshTokenGuard)
  postAccessToken(@Headers('authorization') rawToken: string) {
    const token = this.authService.extractTokenFromHeader(rawToken, true);
    const newToken = this.authService.rotateToken(token, false);
    return {
      accessToken: newToken,
    };
  }

  @Post('token/refresh')
  @UseGuards(RefreshTokenGuard)
  postRefreshToken(@Headers('authorization') rawToken: string) {
    const token = this.authService.extractTokenFromHeader(rawToken, true);
    const newToken = this.authService.rotateToken(token, true);
    return {
      refreshToken: newToken,
    };
  }

  // @Post('login')
  // login(@Headers('authorization') rawToken: string) {
  //   const getToken = this.authService.extractTokenFromHeader(rawToken, false);
  //
  //   const credentials = this.authService.decodeBasicToken(getToken);
  //
  //   return this.authService.loginWithEmail(credentials);
  // }
}
