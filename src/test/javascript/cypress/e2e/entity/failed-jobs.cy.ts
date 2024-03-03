import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('FailedJobs e2e test', () => {
  const failedJobsPageUrl = '/failed-jobs';
  const failedJobsPageUrlPattern = new RegExp('/failed-jobs(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const failedJobsSample = {
    uuid: 'facilitate willfully',
    connection: 'cautiously tenderly robust',
    queue: 'revolving forked anenst',
    payload: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
    exception: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
    failedAt: '2024-03-02T22:21:36.904Z',
  };

  let failedJobs;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/failed-jobs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/failed-jobs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/failed-jobs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (failedJobs) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/failed-jobs/${failedJobs.id}`,
      }).then(() => {
        failedJobs = undefined;
      });
    }
  });

  it('FailedJobs menu should load FailedJobs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('failed-jobs');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FailedJobs').should('exist');
    cy.url().should('match', failedJobsPageUrlPattern);
  });

  describe('FailedJobs page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(failedJobsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FailedJobs page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/failed-jobs/new$'));
        cy.getEntityCreateUpdateHeading('FailedJobs');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', failedJobsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/failed-jobs',
          body: failedJobsSample,
        }).then(({ body }) => {
          failedJobs = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/failed-jobs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/failed-jobs?page=0&size=20>; rel="last",<http://localhost/api/failed-jobs?page=0&size=20>; rel="first"',
              },
              body: [failedJobs],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(failedJobsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FailedJobs page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('failedJobs');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', failedJobsPageUrlPattern);
      });

      it('edit button click should load edit FailedJobs page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FailedJobs');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', failedJobsPageUrlPattern);
      });

      it('edit button click should load edit FailedJobs page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FailedJobs');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', failedJobsPageUrlPattern);
      });

      it('last delete button click should delete instance of FailedJobs', () => {
        cy.intercept('GET', '/api/failed-jobs/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('failedJobs').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', failedJobsPageUrlPattern);

        failedJobs = undefined;
      });
    });
  });

  describe('new FailedJobs page', () => {
    beforeEach(() => {
      cy.visit(`${failedJobsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FailedJobs');
    });

    it('should create an instance of FailedJobs', () => {
      cy.get(`[data-cy="uuid"]`).type('sonnet federation metronome');
      cy.get(`[data-cy="uuid"]`).should('have.value', 'sonnet federation metronome');

      cy.get(`[data-cy="connection"]`).type('jubilant gadzooks');
      cy.get(`[data-cy="connection"]`).should('have.value', 'jubilant gadzooks');

      cy.get(`[data-cy="queue"]`).type('sympathetically snappy annually');
      cy.get(`[data-cy="queue"]`).should('have.value', 'sympathetically snappy annually');

      cy.get(`[data-cy="payload"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="payload"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="exception"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="exception"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="failedAt"]`).type('2024-03-02T21:16');
      cy.get(`[data-cy="failedAt"]`).blur();
      cy.get(`[data-cy="failedAt"]`).should('have.value', '2024-03-02T21:16');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        failedJobs = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', failedJobsPageUrlPattern);
    });
  });
});
