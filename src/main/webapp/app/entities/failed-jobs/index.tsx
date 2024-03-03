import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FailedJobs from './failed-jobs';
import FailedJobsDetail from './failed-jobs-detail';
import FailedJobsUpdate from './failed-jobs-update';
import FailedJobsDeleteDialog from './failed-jobs-delete-dialog';

const FailedJobsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FailedJobs />} />
    <Route path="new" element={<FailedJobsUpdate />} />
    <Route path=":id">
      <Route index element={<FailedJobsDetail />} />
      <Route path="edit" element={<FailedJobsUpdate />} />
      <Route path="delete" element={<FailedJobsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FailedJobsRoutes;
