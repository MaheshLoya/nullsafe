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

describe('Orders e2e test', () => {
  const ordersPageUrl = '/orders';
  const ordersPageUrlPattern = new RegExp('/orders(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const ordersSample = {"orderAmount":26332.03,"price":1069.08,"mrp":32156.93,"tax":29994.04,"status":13807,"orderStatus":false};

  let orders;
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
      body: {"walletAmount":32748.64,"email":"Junius.Spinka-Mosciski@hotmail.com","phone":"(904) 504-2635 x69142","emailVerifiedAt":"2024-03-02T22:12:25.992Z","password":"woot","rememberToken":"over quietly instead","createdAt":"2024-03-02T09:46:34.835Z","updatedAt":"2024-03-02T23:46:37.801Z","name":"quartz","fcm":"meh selfishly extremely","subscriptionAmount":30118},
    }).then(({ body }) => {
      users = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/products',
      body: {"title":"rigidly ew","qtyText":"rework angrily noteworthy","stockQty":17701,"price":11109.07,"tax":20237.89,"mrp":31519.16,"offerText":"far-flung huzzah list","description":"even fatally","disclaimer":"eminent","subscription":false,"createdAt":"2024-03-02T19:36:38.074Z","updatedAt":"2024-03-03T07:13:07.537Z","isActive":true},
    }).then(({ body }) => {
      product = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-addresses',
      body: {"userId":22368,"name":"alienate innocent","sPhone":"pace what runny","flatNo":"roll during border","apartmentName":"ah fictionalize","area":"baulk pfft spoon","landmark":"jaunty","city":"Kendall","pincode":2467,"lat":27495.06,"lng":6269.59,"createdAt":"2024-03-02T16:28:54.018Z","updatedAt":"2024-03-03T05:09:10.013Z","isActive":true},
    }).then(({ body }) => {
      userAddress = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/orders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/orders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/orders/*').as('deleteEntityRequest');
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

    cy.intercept('GET', '/api/order-user-assigns', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/subscribed-order-deliveries', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (orders) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/orders/${orders.id}`,
      }).then(() => {
        orders = undefined;
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

  it('Orders menu should load Orders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('orders');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Orders').should('exist');
    cy.url().should('match', ordersPageUrlPattern);
  });

  describe('Orders page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ordersPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Orders page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/orders/new$'));
        cy.getEntityCreateUpdateHeading('Orders');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/orders',
          body: {
            ...ordersSample,
            user: users,
            product: product,
            address: userAddress,
          },
        }).then(({ body }) => {
          orders = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/orders+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/orders?page=0&size=20>; rel="last",<http://localhost/api/orders?page=0&size=20>; rel="first"',
              },
              body: [orders],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(ordersPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(ordersPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Orders page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orders');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersPageUrlPattern);
      });

      it('edit button click should load edit Orders page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Orders');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersPageUrlPattern);
      });

      it('edit button click should load edit Orders page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Orders');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Orders', () => {
        cy.intercept('GET', '/api/orders/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('orders').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ordersPageUrlPattern);

        orders = undefined;
      });
    });
  });

  describe('new Orders page', () => {
    beforeEach(() => {
      cy.visit(`${ordersPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Orders');
    });

    it.skip('should create an instance of Orders', () => {
      cy.get(`[data-cy="orderType"]`).type('15929');
      cy.get(`[data-cy="orderType"]`).should('have.value', '15929');

      cy.get(`[data-cy="orderAmount"]`).type('25745.92');
      cy.get(`[data-cy="orderAmount"]`).should('have.value', '25745.92');

      cy.get(`[data-cy="price"]`).type('18609.37');
      cy.get(`[data-cy="price"]`).should('have.value', '18609.37');

      cy.get(`[data-cy="mrp"]`).type('3832.46');
      cy.get(`[data-cy="mrp"]`).should('have.value', '3832.46');

      cy.get(`[data-cy="tax"]`).type('21283.25');
      cy.get(`[data-cy="tax"]`).should('have.value', '21283.25');

      cy.get(`[data-cy="qty"]`).type('10386');
      cy.get(`[data-cy="qty"]`).should('have.value', '10386');

      cy.get(`[data-cy="selectedDaysForWeekly"]`).type('movement how');
      cy.get(`[data-cy="selectedDaysForWeekly"]`).should('have.value', 'movement how');

      cy.get(`[data-cy="startDate"]`).type('2024-03-03');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-03-03');

      cy.get(`[data-cy="subscriptionType"]`).type('30091');
      cy.get(`[data-cy="subscriptionType"]`).should('have.value', '30091');

      cy.get(`[data-cy="status"]`).type('14736');
      cy.get(`[data-cy="status"]`).should('have.value', '14736');

      cy.get(`[data-cy="deliveryStatus"]`).type('2334');
      cy.get(`[data-cy="deliveryStatus"]`).should('have.value', '2334');

      cy.get(`[data-cy="orderStatus"]`).should('not.be.checked');
      cy.get(`[data-cy="orderStatus"]`).click();
      cy.get(`[data-cy="orderStatus"]`).should('be.checked');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T19:11');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T19:11');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-03T04:02');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-03T04:02');

      cy.get(`[data-cy="user"]`).select(1);
      cy.get(`[data-cy="product"]`).select(1);
      cy.get(`[data-cy="address"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        orders = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', ordersPageUrlPattern);
    });
  });
});
