import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SubCat from './sub-cat';
import SubCatDetail from './sub-cat-detail';
import SubCatUpdate from './sub-cat-update';
import SubCatDeleteDialog from './sub-cat-delete-dialog';

const SubCatRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SubCat />} />
    <Route path="new" element={<SubCatUpdate />} />
    <Route path=":id">
      <Route index element={<SubCatDetail />} />
      <Route path="edit" element={<SubCatUpdate />} />
      <Route path="delete" element={<SubCatDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SubCatRoutes;
