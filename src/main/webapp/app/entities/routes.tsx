import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AllowPincode from './allow-pincode';
import AppSettings from './app-settings';
import AssignRole from './assign-role';
import AvailableDeliveryLocation from './available-delivery-location';
import BannerImage from './banner-image';
import Cart from './cart';
import Cat from './cat';
import City from './city';
import FailedJobs from './failed-jobs';
import Files from './files';
import Images from './images';
import InvoiceSetting from './invoice-setting';
import Migrations from './migrations';
import OrderUserAssign from './order-user-assign';
import Orders from './orders';
import PasswordResets from './password-resets';
import PaymentGateway from './payment-gateway';
import PersonalAccessTokens from './personal-access-tokens';
import Product from './product';
import Refunds from './refunds';
import Role from './role';
import SocialMedia from './social-media';
import SpecificNotification from './specific-notification';
import SubCat from './sub-cat';
import SubscribedOrderDelivery from './subscribed-order-delivery';
import SubscribedOrders from './subscribed-orders';
import SubscriptionRenewal from './subscription-renewal';
import Testimonials from './testimonials';
import Transactions from './transactions';
import UserAddress from './user-address';
import UserHoliday from './user-holiday';
import UserNotification from './user-notification';
import Users from './users';
import WebAppSettings from './web-app-settings';
import WebPages from './web-pages';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="allow-pincode/*" element={<AllowPincode />} />
        <Route path="app-settings/*" element={<AppSettings />} />
        <Route path="assign-role/*" element={<AssignRole />} />
        <Route path="available-delivery-location/*" element={<AvailableDeliveryLocation />} />
        <Route path="banner-image/*" element={<BannerImage />} />
        <Route path="cart/*" element={<Cart />} />
        <Route path="cat/*" element={<Cat />} />
        <Route path="city/*" element={<City />} />
        <Route path="failed-jobs/*" element={<FailedJobs />} />
        <Route path="files/*" element={<Files />} />
        <Route path="images/*" element={<Images />} />
        <Route path="invoice-setting/*" element={<InvoiceSetting />} />
        <Route path="migrations/*" element={<Migrations />} />
        <Route path="order-user-assign/*" element={<OrderUserAssign />} />
        <Route path="orders/*" element={<Orders />} />
        <Route path="password-resets/*" element={<PasswordResets />} />
        <Route path="payment-gateway/*" element={<PaymentGateway />} />
        <Route path="personal-access-tokens/*" element={<PersonalAccessTokens />} />
        <Route path="product/*" element={<Product />} />
        <Route path="refunds/*" element={<Refunds />} />
        <Route path="role/*" element={<Role />} />
        <Route path="social-media/*" element={<SocialMedia />} />
        <Route path="specific-notification/*" element={<SpecificNotification />} />
        <Route path="sub-cat/*" element={<SubCat />} />
        <Route path="subscribed-order-delivery/*" element={<SubscribedOrderDelivery />} />
        <Route path="subscribed-orders/*" element={<SubscribedOrders />} />
        <Route path="subscription-renewal/*" element={<SubscriptionRenewal />} />
        <Route path="testimonials/*" element={<Testimonials />} />
        <Route path="transactions/*" element={<Transactions />} />
        <Route path="user-address/*" element={<UserAddress />} />
        <Route path="user-holiday/*" element={<UserHoliday />} />
        <Route path="user-notification/*" element={<UserNotification />} />
        <Route path="users/*" element={<Users />} />
        <Route path="web-app-settings/*" element={<WebAppSettings />} />
        <Route path="web-pages/*" element={<WebPages />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
