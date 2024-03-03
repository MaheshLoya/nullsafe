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

describe('Refunds e2e test', () => {
  const refundsPageUrl = '/refunds';
  const refundsPageUrlPattern = new RegExp('/refunds(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const refundsSample = { createdAt: '2024-03-03T02:34:34.552Z', updatedAt: '2024-03-02T20:20:59.729Z' };

  let refunds;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/refunds+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/refunds').as('postEntityRequest');
    cy.intercept('DELETE', '/api/refunds/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (refunds) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/refunds/${refunds.id}`,
      }).then(() => {
        refunds = undefined;
      });
    }
  });

  it('Refunds menu should load Refunds page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('refunds');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Refunds').should('exist');
    cy.url().should('match', refundsPageUrlPattern);
  });

  describe('Refunds page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(refundsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Refunds page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/refunds/new$'));
        cy.getEntityCreateUpdateHeading('Refunds');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', refundsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/refunds',
          body: refundsSample,
        }).then(({ body }) => {
          refunds = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/refunds+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/refunds?page=0&size=20>; rel="last",<http://localhost/api/refunds?page=0&size=20>; rel="first"',
              },
              body: [refunds],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(refundsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Refunds page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('refunds');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', refundsPageUrlPattern);
      });

      it('edit button click should load edit Refunds page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Refunds');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', refundsPageUrlPattern);
      });

      it('edit button click should load edit Refunds page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Refunds');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', refundsPageUrlPattern);
      });

      it('last delete button click should delete instance of Refunds', () => {
        cy.intercept('GET', '/api/refunds/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('refunds').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', refundsPageUrlPattern);

        refunds = undefined;
      });
    });
  });

  describe('new Refunds page', () => {
    beforeEach(() => {
      cy.visit(`${refundsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Refunds');
    });

    it('should create an instance of Refunds', () => {
      cy.get(`[data-cy="orderId"]`).type('14461');
      cy.get(`[data-cy="orderId"]`).should('have.value', '14461');

      cy.get(`[data-cy="transactionId"]`).type('cheerfully for');
      cy.get(`[data-cy="transactionId"]`).should('have.value', 'cheerfully for');

      cy.get(`[data-cy="razorpayRefundId"]`).type('ew');
      cy.get(`[data-cy="razorpayRefundId"]`).should('have.value', 'ew');

      cy.get(`[data-cy="razorpayPaymentId"]`).type('amidst');
      cy.get(`[data-cy="razorpayPaymentId"]`).should('have.value', 'amidst');

      cy.get(`[data-cy="amount"]`).type('31839.13');
      cy.get(`[data-cy="amount"]`).should('have.value', '31839.13');

      cy.get(`[data-cy="currency"]`).type('rig');
      cy.get(`[data-cy="currency"]`).should('have.value', 'rig');

      cy.get(`[data-cy="status"]`).type('slouch circa as');
      cy.get(`[data-cy="status"]`).should('have.value', 'slouch circa as');

      cy.get(`[data-cy="createdBy"]`).type('rank creolise tiptoe');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'rank creolise tiptoe');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T18:50');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T18:50');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T23:02');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T23:02');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        refunds = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', refundsPageUrlPattern);
    });
  });
});
