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

describe('SubscriptionRenewal e2e test', () => {
  const subscriptionRenewalPageUrl = '/subscription-renewal';
  const subscriptionRenewalPageUrlPattern = new RegExp('/subscription-renewal(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const subscriptionRenewalSample = {
    userId: 18346,
    orderId: 10688,
    renewalDate: '2024-03-02',
    paidRenewalAmount: 27302.31,
    status: false,
    startDate: '2024-03-03',
    endDate: '2024-03-02',
    createdAt: '2024-03-02T16:28:01.504Z',
    updatedAt: '2024-03-02T16:44:45.506Z',
  };

  let subscriptionRenewal;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/subscription-renewals+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/subscription-renewals').as('postEntityRequest');
    cy.intercept('DELETE', '/api/subscription-renewals/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (subscriptionRenewal) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/subscription-renewals/${subscriptionRenewal.id}`,
      }).then(() => {
        subscriptionRenewal = undefined;
      });
    }
  });

  it('SubscriptionRenewals menu should load SubscriptionRenewals page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('subscription-renewal');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SubscriptionRenewal').should('exist');
    cy.url().should('match', subscriptionRenewalPageUrlPattern);
  });

  describe('SubscriptionRenewal page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(subscriptionRenewalPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SubscriptionRenewal page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/subscription-renewal/new$'));
        cy.getEntityCreateUpdateHeading('SubscriptionRenewal');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionRenewalPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/subscription-renewals',
          body: subscriptionRenewalSample,
        }).then(({ body }) => {
          subscriptionRenewal = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/subscription-renewals+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/subscription-renewals?page=0&size=20>; rel="last",<http://localhost/api/subscription-renewals?page=0&size=20>; rel="first"',
              },
              body: [subscriptionRenewal],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(subscriptionRenewalPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SubscriptionRenewal page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('subscriptionRenewal');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionRenewalPageUrlPattern);
      });

      it('edit button click should load edit SubscriptionRenewal page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubscriptionRenewal');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionRenewalPageUrlPattern);
      });

      it('edit button click should load edit SubscriptionRenewal page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubscriptionRenewal');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionRenewalPageUrlPattern);
      });

      it('last delete button click should delete instance of SubscriptionRenewal', () => {
        cy.intercept('GET', '/api/subscription-renewals/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('subscriptionRenewal').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionRenewalPageUrlPattern);

        subscriptionRenewal = undefined;
      });
    });
  });

  describe('new SubscriptionRenewal page', () => {
    beforeEach(() => {
      cy.visit(`${subscriptionRenewalPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SubscriptionRenewal');
    });

    it('should create an instance of SubscriptionRenewal', () => {
      cy.get(`[data-cy="userId"]`).type('1034');
      cy.get(`[data-cy="userId"]`).should('have.value', '1034');

      cy.get(`[data-cy="orderId"]`).type('14544');
      cy.get(`[data-cy="orderId"]`).should('have.value', '14544');

      cy.get(`[data-cy="transactionId"]`).type('384');
      cy.get(`[data-cy="transactionId"]`).should('have.value', '384');

      cy.get(`[data-cy="renewalDate"]`).type('2024-03-02');
      cy.get(`[data-cy="renewalDate"]`).blur();
      cy.get(`[data-cy="renewalDate"]`).should('have.value', '2024-03-02');

      cy.get(`[data-cy="paidRenewalAmount"]`).type('3185.34');
      cy.get(`[data-cy="paidRenewalAmount"]`).should('have.value', '3185.34');

      cy.get(`[data-cy="status"]`).should('not.be.checked');
      cy.get(`[data-cy="status"]`).click();
      cy.get(`[data-cy="status"]`).should('be.checked');

      cy.get(`[data-cy="startDate"]`).type('2024-03-02');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-03-02');

      cy.get(`[data-cy="endDate"]`).type('2024-03-03');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-03-03');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-03T06:59');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-03T06:59');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T12:13');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T12:13');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        subscriptionRenewal = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', subscriptionRenewalPageUrlPattern);
    });
  });
});
