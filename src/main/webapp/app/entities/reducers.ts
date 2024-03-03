import allowPincode from 'app/entities/allow-pincode/allow-pincode.reducer';
import appSettings from 'app/entities/app-settings/app-settings.reducer';
import assignRole from 'app/entities/assign-role/assign-role.reducer';
import availableDeliveryLocation from 'app/entities/available-delivery-location/available-delivery-location.reducer';
import bannerImage from 'app/entities/banner-image/banner-image.reducer';
import cart from 'app/entities/cart/cart.reducer';
import cat from 'app/entities/cat/cat.reducer';
import city from 'app/entities/city/city.reducer';
import failedJobs from 'app/entities/failed-jobs/failed-jobs.reducer';
import files from 'app/entities/files/files.reducer';
import images from 'app/entities/images/images.reducer';
import invoiceSetting from 'app/entities/invoice-setting/invoice-setting.reducer';
import migrations from 'app/entities/migrations/migrations.reducer';
import orderUserAssign from 'app/entities/order-user-assign/order-user-assign.reducer';
import orders from 'app/entities/orders/orders.reducer';
import passwordResets from 'app/entities/password-resets/password-resets.reducer';
import paymentGateway from 'app/entities/payment-gateway/payment-gateway.reducer';
import personalAccessTokens from 'app/entities/personal-access-tokens/personal-access-tokens.reducer';
import product from 'app/entities/product/product.reducer';
import refunds from 'app/entities/refunds/refunds.reducer';
import role from 'app/entities/role/role.reducer';
import socialMedia from 'app/entities/social-media/social-media.reducer';
import specificNotification from 'app/entities/specific-notification/specific-notification.reducer';
import subCat from 'app/entities/sub-cat/sub-cat.reducer';
import subscribedOrderDelivery from 'app/entities/subscribed-order-delivery/subscribed-order-delivery.reducer';
import subscribedOrders from 'app/entities/subscribed-orders/subscribed-orders.reducer';
import subscriptionRenewal from 'app/entities/subscription-renewal/subscription-renewal.reducer';
import testimonials from 'app/entities/testimonials/testimonials.reducer';
import transactions from 'app/entities/transactions/transactions.reducer';
import userAddress from 'app/entities/user-address/user-address.reducer';
import userHoliday from 'app/entities/user-holiday/user-holiday.reducer';
import userNotification from 'app/entities/user-notification/user-notification.reducer';
import users from 'app/entities/users/users.reducer';
import webAppSettings from 'app/entities/web-app-settings/web-app-settings.reducer';
import webPages from 'app/entities/web-pages/web-pages.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  allowPincode,
  appSettings,
  assignRole,
  availableDeliveryLocation,
  bannerImage,
  cart,
  cat,
  city,
  failedJobs,
  files,
  images,
  invoiceSetting,
  migrations,
  orderUserAssign,
  orders,
  passwordResets,
  paymentGateway,
  personalAccessTokens,
  product,
  refunds,
  role,
  socialMedia,
  specificNotification,
  subCat,
  subscribedOrderDelivery,
  subscribedOrders,
  subscriptionRenewal,
  testimonials,
  transactions,
  userAddress,
  userHoliday,
  userNotification,
  users,
  webAppSettings,
  webPages,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
