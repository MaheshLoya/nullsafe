import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Testimonials from './testimonials';
import TestimonialsDetail from './testimonials-detail';
import TestimonialsUpdate from './testimonials-update';
import TestimonialsDeleteDialog from './testimonials-delete-dialog';

const TestimonialsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Testimonials />} />
    <Route path="new" element={<TestimonialsUpdate />} />
    <Route path=":id">
      <Route index element={<TestimonialsDetail />} />
      <Route path="edit" element={<TestimonialsUpdate />} />
      <Route path="delete" element={<TestimonialsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestimonialsRoutes;
