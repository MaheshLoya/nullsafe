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

describe('SubscribedOrders e2e test', () => {
  const subscribedOrdersPageUrl = '/subscribed-orders';
  const subscribedOrdersPageUrlPattern = new RegExp('/subscribed-orders(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const subscribedOrdersSample = {"orderAmount":15004.07,"price":23317.75,"mrp":25714.89,"tax":24210.05,"startDate":"2024-03-02","endDate":"2024-03-02","approvalStatus":28097,"orderStatus":false};

  let subscribedOrders;
  // let users;
  // let product;
  // let userAddress;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"walletAmount":13450.81,"email":"Celia_Ortiz44@yahoo.com","phone":"305-387-1694","emailVerifiedAt":"2024-03-02T19:54:01.967Z","password":"before enormous once","rememberToken":"till quarterly","createdAt":"2024-03-03T00:42:11.223Z","updatedAt":"2024-03-02T10:26:28.124Z","name":"huzzah","fcm":"memorialise","subscriptionAmount":29872},
    }).then(({ body }) => {
      users = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/products',
      body: {"title":"yippee psst","qtyText":"interestingly","stockQty":12736,"price":16389.67,"tax":31852.75,"mrp":26410.97,"offerText":"yet","description":"bashfully","disclaimer":"phew","subscription":true,"createdAt":"2024-03-02T13:41:30.518Z","updatedAt":"2024-03-03T04:21:21.636Z","isActive":true},
    }).then(({ body }) => {
      product = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-addresses',
      body: {"userId":4947,"name":"deal blood","sPhone":"failing reassess","flatNo":"boastfully","apartmentName":"patiently quicker hence","area":"resect decamp over","landmark":"plus whose analgesia","city":"Port Floy","pincode":10739,"lat":16897.55,"lng":31012.45,"createdAt":"2024-03-02T20:47:18.371Z","updatedAt":"2024-03-03T07:12:22.427Z","isActive":false},
    }).then(({ body }) => {
      userAddress = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/subscribed-orders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/subscribed-orders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/subscribed-orders/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [users],
    });

    cy.intercept('GET', '/api/transactions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

    cy.intercept('GET', '/api/user-addresses', {
      statusCode: 200,
      body: [userAddress],
    });

  });
   */

  afterEach(() => {
    if (subscribedOrders) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/subscribed-orders/${subscribedOrders.id}`,
      }).then(() => {
        subscribedOrders = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (users) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${users.id}`,
      }).then(() => {
        users = undefined;
      });
    }
    if (product) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/products/${product.id}`,
      }).then(() => {
        product = undefined;
      });
    }
    if (userAddress) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-addresses/${userAddress.id}`,
      }).then(() => {
        userAddress = undefined;
      });
    }
  });
   */

  it('SubscribedOrders menu should load SubscribedOrders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('subscribed-orders');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SubscribedOrders').should('exist');
    cy.url().should('match', subscribedOrdersPageUrlPattern);
  });

  describe('SubscribedOrders page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(subscribedOrdersPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SubscribedOrders page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/subscribed-orders/new$'));
        cy.getEntityCreateUpdateHeading('SubscribedOrders');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscribedOrdersPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/subscribed-orders',
          body: {
            ...subscribedOrdersSample,
            user: users,
            product: product,
            address: userAddress,
          },
        }).then(({ body }) => {
          subscribedOrders = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/subscribed-orders+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/subscribed-orders?page=0&size=20>; rel="last",<http://localhost/api/subscribed-orders?page=0&size=20>; rel="first"',
              },
              body: [subscribedOrders],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(subscribedOrdersPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(subscribedOrdersPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details SubscribedOrders page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('subscribedOrders');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscribedOrdersPageUrlPattern);
      });

      it('edit button click should load edit SubscribedOrders page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubscribedOrders');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscribedOrdersPageUrlPattern);
      });

      it('edit button click should load edit SubscribedOrders page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubscribedOrders');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscribedOrdersPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of SubscribedOrders', () => {
        cy.intercept('GET', '/api/subscribed-orders/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('subscribedOrders').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscribedOrdersPageUrlPattern);

        subscribedOrders = undefined;
      });
    });
  });

  describe('new SubscribedOrders page', () => {
    beforeEach(() => {
      cy.visit(`${subscribedOrdersPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SubscribedOrders');
    });

    it.skip('should create an instance of SubscribedOrders', () => {
      cy.get(`[data-cy="paymentType"]`).type('25930');
      cy.get(`[data-cy="paymentType"]`).should('have.value', '25930');

      cy.get(`[data-cy="orderAmount"]`).type('16782.03');
      cy.get(`[data-cy="orderAmount"]`).should('have.value', '16782.03');

      cy.get(`[data-cy="subscriptionBalanceAmount"]`).type('12413.7');
      cy.get(`[data-cy="subscriptionBalanceAmount"]`).should('have.value', '12413.7');

      cy.get(`[data-cy="price"]`).type('30924.78');
      cy.get(`[data-cy="price"]`).should('have.value', '30924.78');

      cy.get(`[data-cy="mrp"]`).type('2243.65');
      cy.get(`[data-cy="mrp"]`).should('have.value', '2243.65');

      cy.get(`[data-cy="tax"]`).type('23642.81');
      cy.get(`[data-cy="tax"]`).should('have.value', '23642.81');

      cy.get(`[data-cy="qty"]`).type('28591');
      cy.get(`[data-cy="qty"]`).should('have.value', '28591');

      cy.get(`[data-cy="offerId"]`).type('13248');
      cy.get(`[data-cy="offerId"]`).should('have.value', '13248');

      cy.get(`[data-cy="selectedDaysForWeekly"]`).type('gah');
      cy.get(`[data-cy="selectedDaysForWeekly"]`).should('have.value', 'gah');

      cy.get(`[data-cy="startDate"]`).type('2024-03-02');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-03-02');

      cy.get(`[data-cy="endDate"]`).type('2024-03-03');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-03-03');

      cy.get(`[data-cy="lastRenewalDate"]`).type('2024-03-03');
      cy.get(`[data-cy="lastRenewalDate"]`).blur();
      cy.get(`[data-cy="lastRenewalDate"]`).should('have.value', '2024-03-03');

      cy.get(`[data-cy="subscriptionType"]`).type('218');
      cy.get(`[data-cy="subscriptionType"]`).should('have.value', '218');

      cy.get(`[data-cy="approvalStatus"]`).type('15222');
      cy.get(`[data-cy="approvalStatus"]`).should('have.value', '15222');

      cy.get(`[data-cy="orderStatus"]`).should('not.be.checked');
      cy.get(`[data-cy="orderStatus"]`).click();
      cy.get(`[data-cy="orderStatus"]`).should('be.checked');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-03T02:05');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-03T02:05');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T11:07');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T11:07');

      cy.get(`[data-cy="createdBy"]`).type('phew');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'phew');

      cy.get(`[data-cy="updatedBy"]`).type('following fairness');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', 'following fairness');

      cy.get(`[data-cy="user"]`).select(1);
      cy.get(`[data-cy="product"]`).select(1);
      cy.get(`[data-cy="address"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        subscribedOrders = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', subscribedOrdersPageUrlPattern);
    });
  });
});
