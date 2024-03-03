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

describe('Transactions e2e test', () => {
  const transactionsPageUrl = '/transactions';
  const transactionsPageUrlPattern = new RegExp('/transactions(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const transactionsSample = { amount: 6406.13, paymentMode: 19658 };

  let transactions;
  let users;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {
        walletAmount: 25056.49,
        email: 'Murl_Dickens31@yahoo.com',
        phone: '1-367-702-7912',
        emailVerifiedAt: '2024-03-02T22:14:46.078Z',
        password: 'go stepson',
        rememberToken: 'muffled',
        createdAt: '2024-03-02T15:27:53.303Z',
        updatedAt: '2024-03-02T13:18:11.422Z',
        name: 'stamina although jovially',
        fcm: 'earth',
        subscriptionAmount: 14238,
      },
    }).then(({ body }) => {
      users = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/transactions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/transactions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/transactions/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/orders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [users],
    });

    cy.intercept('GET', '/api/subscribed-orders', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (transactions) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transactions/${transactions.id}`,
      }).then(() => {
        transactions = undefined;
      });
    }
  });

  afterEach(() => {
    if (users) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${users.id}`,
      }).then(() => {
        users = undefined;
      });
    }
  });

  it('Transactions menu should load Transactions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('transactions');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Transactions').should('exist');
    cy.url().should('match', transactionsPageUrlPattern);
  });

  describe('Transactions page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(transactionsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Transactions page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/transactions/new$'));
        cy.getEntityCreateUpdateHeading('Transactions');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/transactions',
          body: {
            ...transactionsSample,
            user: users,
          },
        }).then(({ body }) => {
          transactions = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/transactions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/transactions?page=0&size=20>; rel="last",<http://localhost/api/transactions?page=0&size=20>; rel="first"',
              },
              body: [transactions],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(transactionsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Transactions page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('transactions');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionsPageUrlPattern);
      });

      it('edit button click should load edit Transactions page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Transactions');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionsPageUrlPattern);
      });

      it('edit button click should load edit Transactions page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Transactions');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionsPageUrlPattern);
      });

      it('last delete button click should delete instance of Transactions', () => {
        cy.intercept('GET', '/api/transactions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('transactions').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionsPageUrlPattern);

        transactions = undefined;
      });
    });
  });

  describe('new Transactions page', () => {
    beforeEach(() => {
      cy.visit(`${transactionsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Transactions');
    });

    it('should create an instance of Transactions', () => {
      cy.get(`[data-cy="paymentId"]`).type('ostracize wherever really');
      cy.get(`[data-cy="paymentId"]`).should('have.value', 'ostracize wherever really');

      cy.get(`[data-cy="amount"]`).type('14164.01');
      cy.get(`[data-cy="amount"]`).should('have.value', '14164.01');

      cy.get(`[data-cy="description"]`).type('aboard fix');
      cy.get(`[data-cy="description"]`).should('have.value', 'aboard fix');

      cy.get(`[data-cy="type"]`).type('23813');
      cy.get(`[data-cy="type"]`).should('have.value', '23813');

      cy.get(`[data-cy="paymentMode"]`).type('12428');
      cy.get(`[data-cy="paymentMode"]`).should('have.value', '12428');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-03T00:27');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-03T00:27');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T17:37');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T17:37');

      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        transactions = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', transactionsPageUrlPattern);
    });
  });
});
