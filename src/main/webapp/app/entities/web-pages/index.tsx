import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import WebPages from './web-pages';
import WebPagesDetail from './web-pages-detail';
import WebPagesUpdate from './web-pages-update';
import WebPagesDeleteDialog from './web-pages-delete-dialog';

const WebPagesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<WebPages />} />
    <Route path="new" element={<WebPagesUpdate />} />
    <Route path=":id">
      <Route index element={<WebPagesDetail />} />
      <Route path="edit" element={<WebPagesUpdate />} />
      <Route path="delete" element={<WebPagesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default WebPagesRoutes;
