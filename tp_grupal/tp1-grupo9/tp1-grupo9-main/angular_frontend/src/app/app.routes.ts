// @ts-ignore
import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component'
import { ShopComponent } from './shop/shop.component'
import { AdminComponent } from './admin/admin.component'
import { CartComponent } from './cart/cart.component'
import { UserComponent } from './user/user.component'
import { OrdersComponent } from './orders/orders.component'
import { ForgotPasswordComponent } from "./forgot-password/forgot-password.component";
import { DevideOrderComponent } from "./devide-order/devide-order.component";
import { EditProfileComponent } from "./edit-profile/edit-profile.component";
import { ChooseAvatarComponent } from "./choose-avatar/choose-avatar.component";
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';

export const routes: Routes = [
    { path: '', component: LoginComponent, pathMatch: 'full' }, //el default path va a llevar a la login page
    { path: 'register', component: RegisterComponent },
    { path: 'shop', component: ShopComponent },
    { path: 'admin', component: AdminComponent },
    { path: 'cart', component: CartComponent },
    { path: 'orders', component: OrdersComponent },
    { path: 'user', component: UserComponent },
    { path: 'forgotPassword', component: ForgotPasswordComponent },
    { path: 'devideOrder', component: DevideOrderComponent },
    { path: 'editProfile', component: EditProfileComponent },
    { path: 'chooseAvatar', component: ChooseAvatarComponent },
    { path: 'chooseAvatar', component: ChooseAvatarComponent },
    { path: '**', component: PageNotFoundComponent },
];
