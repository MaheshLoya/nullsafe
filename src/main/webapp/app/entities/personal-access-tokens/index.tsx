import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PersonalAccessTokens from './personal-access-tokens';
import PersonalAccessTokensDetail from './personal-access-tokens-detail';
import PersonalAccessTokensUpdate from './personal-access-tokens-update';
import PersonalAccessTokensDeleteDialog from './personal-access-tokens-delete-dialog';

const PersonalAccessTokensRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PersonalAccessTokens />} />
    <Route path="new" element={<PersonalAccessTokensUpdate />} />
    <Route path=":id">
      <Route index element={<PersonalAccessTokensDetail />} />
      <Route path="edit" element={<PersonalAccessTokensUpdate />} />
      <Route path="delete" element={<PersonalAccessTokensDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PersonalAccessTokensRoutes;
