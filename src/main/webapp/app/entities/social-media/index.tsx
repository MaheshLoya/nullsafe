import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SocialMedia from './social-media';
import SocialMediaDetail from './social-media-detail';
import SocialMediaUpdate from './social-media-update';
import SocialMediaDeleteDialog from './social-media-delete-dialog';

const SocialMediaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SocialMedia />} />
    <Route path="new" element={<SocialMediaUpdate />} />
    <Route path=":id">
      <Route index element={<SocialMediaDetail />} />
      <Route path="edit" element={<SocialMediaUpdate />} />
      <Route path="delete" element={<SocialMediaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SocialMediaRoutes;
