import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { BookingMenuEntity } from './entities/booking-menus.entity';
import { Repository } from 'typeorm';
import { BookingEntity } from '../booking/entities/booking.entity';
import { MenusResponseDto } from '../menus/dto/menus-response.dto';

@Injectable()
export class BookingMenusService {
  constructor(
    @InjectRepository(BookingMenuEntity)
    private readonly bookingMenuRepository: Repository<BookingMenuEntity>,
  ) {}

  async createBookingMenu(
    booking: BookingEntity,
    menu: MenusResponseDto,
    quantity: number,
  ) {
    const bookingMenu = this.bookingMenuRepository.create({
      booking,
      menu,
      quantity,
    });
    return await this.bookingMenuRepository.save(bookingMenu);
  }
}
