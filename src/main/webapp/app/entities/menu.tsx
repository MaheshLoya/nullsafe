import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/allow-pincode">
        <Translate contentKey="global.menu.entities.allowPincode" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/app-settings">
        <Translate contentKey="global.menu.entities.appSettings" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/assign-role">
        <Translate contentKey="global.menu.entities.assignRole" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/available-delivery-location">
        <Translate contentKey="global.menu.entities.availableDeliveryLocation" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/banner-image">
        <Translate contentKey="global.menu.entities.bannerImage" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cart">
        <Translate contentKey="global.menu.entities.cart" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cat">
        <Translate contentKey="global.menu.entities.cat" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/city">
        <Translate contentKey="global.menu.entities.city" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/failed-jobs">
        <Translate contentKey="global.menu.entities.failedJobs" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/files">
        <Translate contentKey="global.menu.entities.files" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/images">
        <Translate contentKey="global.menu.entities.images" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/invoice-setting">
        <Translate contentKey="global.menu.entities.invoiceSetting" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/migrations">
        <Translate contentKey="global.menu.entities.migrations" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order-user-assign">
        <Translate contentKey="global.menu.entities.orderUserAssign" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/orders">
        <Translate contentKey="global.menu.entities.orders" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/password-resets">
        <Translate contentKey="global.menu.entities.passwordResets" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment-gateway">
        <Translate contentKey="global.menu.entities.paymentGateway" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/personal-access-tokens">
        <Translate contentKey="global.menu.entities.personalAccessTokens" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/product">
        <Translate contentKey="global.menu.entities.product" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/refunds">
        <Translate contentKey="global.menu.entities.refunds" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/role">
        <Translate contentKey="global.menu.entities.role" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/social-media">
        <Translate contentKey="global.menu.entities.socialMedia" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/specific-notification">
        <Translate contentKey="global.menu.entities.specificNotification" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sub-cat">
        <Translate contentKey="global.menu.entities.subCat" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/subscribed-order-delivery">
        <Translate contentKey="global.menu.entities.subscribedOrderDelivery" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/subscribed-orders">
        <Translate contentKey="global.menu.entities.subscribedOrders" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/subscription-renewal">
        <Translate contentKey="global.menu.entities.subscriptionRenewal" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/testimonials">
        <Translate contentKey="global.menu.entities.testimonials" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/transactions">
        <Translate contentKey="global.menu.entities.transactions" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-address">
        <Translate contentKey="global.menu.entities.userAddress" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-holiday">
        <Translate contentKey="global.menu.entities.userHoliday" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-notification">
        <Translate contentKey="global.menu.entities.userNotification" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/users">
        <Translate contentKey="global.menu.entities.users" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/web-app-settings">
        <Translate contentKey="global.menu.entities.webAppSettings" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/web-pages">
        <Translate contentKey="global.menu.entities.webPages" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
