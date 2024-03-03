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

describe('SubscribedOrderDelivery e2e test', () => {
  const subscribedOrderDeliveryPageUrl = '/subscribed-order-delivery';
  const subscribedOrderDeliveryPageUrlPattern = new RegExp('/subscribed-order-delivery(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const subscribedOrderDeliverySample = {};

  let subscribedOrderDelivery;
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
      body: {"orderType":17935,"orderAmount":29773.83,"price":13702.11,"mrp":32445.34,"tax":32751.29,"qty":14020,"selectedDaysForWeekly":"emigrate","startDate":"2024-03-02","subscriptionType":8935,"status":5213,"deliveryStatus":5860,"orderStatus":true,"createdAt":"2024-03-02T11:18:31.317Z","updatedAt":"2024-03-02T23:00:04.889Z"},
    }).then(({ body }) => {
      orders = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"walletAmount":31617.36,"email":"Rowan.Mohr65@hotmail.com","phone":"1-686-291-5946","emailVerifiedAt":"2024-03-02T18:11:18.134Z","password":"ischemia foxglove","rememberToken":"yippee misty","createdAt":"2024-03-02T08:36:40.563Z","updatedAt":"2024-03-02T14:51:52.648Z","name":"against","fcm":"amidst reword distort","subscriptionAmount":28542},
    }).then(({ body }) => {
      users = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/subscribed-order-deliveries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/subscribed-order-deliveries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/subscribed-order-deliveries/*').as('deleteEntityRequest');
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
    if (subscribedOrderDelivery) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/subscribed-order-deliveries/${subscribedOrderDelivery.id}`,
      }).then(() => {
        subscribedOrderDelivery = undefined;
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

  it('SubscribedOrderDeliveries menu should load SubscribedOrderDeliveries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('subscribed-order-delivery');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SubscribedOrderDelivery').should('exist');
    cy.url().should('match', subscribedOrderDeliveryPageUrlPattern);
  });

  describe('SubscribedOrderDelivery page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(subscribedOrderDeliveryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SubscribedOrderDelivery page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/subscribed-order-delivery/new$'));
        cy.getEntityCreateUpdateHeading('SubscribedOrderDelivery');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscribedOrderDeliveryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/subscribed-order-deliveries',
          body: {
            ...subscribedOrderDeliverySample,
            order: orders,
            entryUser: users,
          },
        }).then(({ body }) => {
          subscribedOrderDelivery = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/subscribed-order-deliveries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/subscribed-order-deliveries?page=0&size=20>; rel="last",<http://localhost/api/subscribed-order-deliveries?page=0&size=20>; rel="first"',
              },
              body: [subscribedOrderDelivery],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(subscribedOrderDeliveryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(subscribedOrderDeliveryPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details SubscribedOrderDelivery page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('subscribedOrderDelivery');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscribedOrderDeliveryPageUrlPattern);
      });

      it('edit button click should load edit SubscribedOrderDelivery page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubscribedOrderDelivery');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscribedOrderDeliveryPageUrlPattern);
      });

      it('edit button click should load edit SubscribedOrderDelivery page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubscribedOrderDelivery');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscribedOrderDeliveryPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of SubscribedOrderDelivery', () => {
        cy.intercept('GET', '/api/subscribed-order-deliveries/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('subscribedOrderDelivery').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscribedOrderDeliveryPageUrlPattern);

        subscribedOrderDelivery = undefined;
      });
    });
  });

  describe('new SubscribedOrderDelivery page', () => {
    beforeEach(() => {
      cy.visit(`${subscribedOrderDeliveryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SubscribedOrderDelivery');
    });

    it.skip('should create an instance of SubscribedOrderDelivery', () => {
      cy.get(`[data-cy="date"]`).type('2024-03-03');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2024-03-03');

      cy.get(`[data-cy="paymentMode"]`).type('6415');
      cy.get(`[data-cy="paymentMode"]`).should('have.value', '6415');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T15:19');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T15:19');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T14:36');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T14:36');

      cy.get(`[data-cy="order"]`).select(1);
      cy.get(`[data-cy="entryUser"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        subscribedOrderDelivery = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', subscribedOrderDeliveryPageUrlPattern);
    });
  });
});
