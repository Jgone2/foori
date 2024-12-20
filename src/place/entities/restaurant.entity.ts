import { BaseModel } from '../../common/entities/base-model';
import { Column, Entity, OneToMany } from 'typeorm';
import { MenuEntity } from '../../menus/entities/menu.entity';
import { ReviewEntity } from '../../reviews/entities/review.entity';
import { BookingEntity } from '../../booking/entities/booking.entity';
import { FavoritesEntity } from '../../common/entities/favorites.entity';

@Entity('restaurants', { schema: 'foori' })
export class RestaurantEntity extends BaseModel {
  @Column({
    name: 'place_name',
    type: 'varchar',
    length: 100,
    comment: '상호명',
  })
  name: string;

  @Column({
    name: 'category',
    type: 'varchar',
    length: 20,
  })
  category: string;

  @Column({
    name: 'address',
    type: 'varchar',
    length: 100,
  })
  address: string;

  @Column({
    name: 'location_num',
    type: 'varchar',
    length: 20,
    comment: '지번',
  })
  locationNum: string;

  @Column({
    name: 'postal_code',
    type: 'varchar',
    length: 10,
    comment: '우편번호',
  })
  postalCode: string;

  @Column({
    name: 'open_days',
    type: 'varchar',
    length: 50,
    comment: '영업요일',
  })
  openDays: string;

  @Column({
    name: 'open_time',
    type: 'time',
    comment: '오픈시간',
  })
  openTime: Date;

  @Column({
    name: 'close_time',
    type: 'time',
    comment: '마감시간',
  })
  closeTime: Date;

  @Column({
    name: 'tel_num',
    type: 'varchar',
    length: 20,
    comment: '전화번호',
  })
  telNum: string;

  @Column({
    name: 'review_count',
    type: 'int',
    default: 0,
    comment: '리뷰 수',
  })
  reviewCount: number;

  @Column({
    name: 'rating_avg',
    type: 'decimal',
    precision: 3,
    scale: 2,
    default: 0,
    comment: '평균 평점',
  })
  ratingAvg: number;

  @OneToMany(() => MenuEntity, (menu) => menu.restaurant, { cascade: true })
  menus: MenuEntity[];

  @OneToMany(() => ReviewEntity, (review) => review.restaurant, {
    cascade: true,
  })
  reviews: ReviewEntity[];

  @OneToMany(() => BookingEntity, (booking) => booking.restaurant, {
    cascade: true,
  })
  bookings: BookingEntity[];

  @OneToMany(() => FavoritesEntity, (favorites) => favorites.restaurant)
  favorites: FavoritesEntity[];
}
