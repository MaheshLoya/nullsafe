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

describe('OrderUserAssign e2e test', () => {
  const orderUserAssignPageUrl = '/order-user-assign';
  const orderUserAssignPageUrlPattern = new RegExp('/order-user-assign(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const orderUserAssignSample = {};

  let orderUserAssign;
  // let orders;
  // let users;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/orders',
      body: {"orderType":1839,"orderAmount":18846.77,"price":12627.21,"mrp":27078.22,"tax":20256.65,"qty":16998,"selectedDaysForWeekly":"absent double","startDate":"2024-03-02","subscriptionType":2219,"status":22522,"deliveryStatus":32639,"orderStatus":true,"createdAt":"2024-03-02T11:31:52.188Z","updatedAt":"2024-03-02T13:42:47.959Z"},
    }).then(({ body }) => {
      orders = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"walletAmount":7858.31,"email":"Ryan.Rippin35@hotmail.com","phone":"(375) 250-3087","emailVerifiedAt":"2024-03-03T07:36:13.664Z","password":"dearly saw tightly","rememberToken":"adventurously until","createdAt":"2024-03-02T23:20:04.765Z","updatedAt":"2024-03-02T16:46:37.394Z","name":"shakily cumbersome","fcm":"aw strut","subscriptionAmount":13300},
    }).then(({ body }) => {
      users = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/order-user-assigns+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-user-assigns').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-user-assigns/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/orders', {
      statusCode: 200,
      body: [orders],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [users],
    });

  });
   */

  afterEach(() => {
    if (orderUserAssign) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-user-assigns/${orderUserAssign.id}`,
      }).then(() => {
        orderUserAssign = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (orders) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/orders/${orders.id}`,
      }).then(() => {
        orders = undefined;
      });
    }
    if (users) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${users.id}`,
      }).then(() => {
        users = undefined;
      });
    }
  });
   */

  it('OrderUserAssigns menu should load OrderUserAssigns page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-user-assign');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderUserAssign').should('exist');
    cy.url().should('match', orderUserAssignPageUrlPattern);
  });

  describe('OrderUserAssign page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderUserAssignPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderUserAssign page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/order-user-assign/new$'));
        cy.getEntityCreateUpdateHeading('OrderUserAssign');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderUserAssignPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-user-assigns',
          body: {
            ...orderUserAssignSample,
            order: orders,
            user: users,
          },
        }).then(({ body }) => {
          orderUserAssign = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-user-assigns+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/order-user-assigns?page=0&size=20>; rel="last",<http://localhost/api/order-user-assigns?page=0&size=20>; rel="first"',
              },
              body: [orderUserAssign],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderUserAssignPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(orderUserAssignPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details OrderUserAssign page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderUserAssign');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderUserAssignPageUrlPattern);
      });

      it('edit button click should load edit OrderUserAssign page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderUserAssign');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderUserAssignPageUrlPattern);
      });

      it('edit button click should load edit OrderUserAssign page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderUserAssign');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderUserAssignPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of OrderUserAssign', () => {
        cy.intercept('GET', '/api/order-user-assigns/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('orderUserAssign').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderUserAssignPageUrlPattern);

        orderUserAssign = undefined;
      });
    });
  });

  describe('new OrderUserAssign page', () => {
    beforeEach(() => {
      cy.visit(`${orderUserAssignPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderUserAssign');
    });

    it.skip('should create an instance of OrderUserAssign', () => {
      cy.get(`[data-cy="createdAt"]`).type('2024-03-03T04:56');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-03T04:56');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T14:02');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T14:02');

      cy.get(`[data-cy="order"]`).select(1);
      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        orderUserAssign = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', orderUserAssignPageUrlPattern);
    });
  });
});
