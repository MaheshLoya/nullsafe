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

describe('Cart e2e test', () => {
  const cartPageUrl = '/cart';
  const cartPageUrlPattern = new RegExp('/cart(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const cartSample = {"qty":10874,"price":24440.26,"totalPrice":27150.94,"mrp":6320.09,"tax":31227.89,"qtyText":"skin"};

  let cart;
  // let product;
  // let users;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/products',
      body: {"title":"viscose sweeten appear","qtyText":"politely cooked athwart","stockQty":19005,"price":25021.38,"tax":21414.9,"mrp":12906.21,"offerText":"noisy leave","description":"merry represent yowza","disclaimer":"haven","subscription":true,"createdAt":"2024-03-02T15:01:39.400Z","updatedAt":"2024-03-02T13:31:15.690Z","isActive":false},
    }).then(({ body }) => {
      product = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"walletAmount":21170.56,"email":"Gussie99@hotmail.com","phone":"724-299-9420 x36144","emailVerifiedAt":"2024-03-02T21:04:31.866Z","password":"before honorable","rememberToken":"mug amongst","createdAt":"2024-03-02T14:04:03.272Z","updatedAt":"2024-03-02T11:43:51.921Z","name":"naturally generally gripping","fcm":"toast mockingly unlearn","subscriptionAmount":18125},
    }).then(({ body }) => {
      users = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/carts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/carts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/carts/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [users],
    });

  });
   */

  afterEach(() => {
    if (cart) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/carts/${cart.id}`,
      }).then(() => {
        cart = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (product) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/products/${product.id}`,
      }).then(() => {
        product = undefined;
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

  it('Carts menu should load Carts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cart');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Cart').should('exist');
    cy.url().should('match', cartPageUrlPattern);
  });

  describe('Cart page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cartPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Cart page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/cart/new$'));
        cy.getEntityCreateUpdateHeading('Cart');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cartPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/carts',
          body: {
            ...cartSample,
            product: product,
            user: users,
          },
        }).then(({ body }) => {
          cart = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/carts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/carts?page=0&size=20>; rel="last",<http://localhost/api/carts?page=0&size=20>; rel="first"',
              },
              body: [cart],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cartPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(cartPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Cart page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cart');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cartPageUrlPattern);
      });

      it('edit button click should load edit Cart page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cart');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cartPageUrlPattern);
      });

      it('edit button click should load edit Cart page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cart');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cartPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Cart', () => {
        cy.intercept('GET', '/api/carts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('cart').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cartPageUrlPattern);

        cart = undefined;
      });
    });
  });

  describe('new Cart page', () => {
    beforeEach(() => {
      cy.visit(`${cartPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Cart');
    });

    it.skip('should create an instance of Cart', () => {
      cy.get(`[data-cy="qty"]`).type('26342');
      cy.get(`[data-cy="qty"]`).should('have.value', '26342');

      cy.get(`[data-cy="price"]`).type('12899.07');
      cy.get(`[data-cy="price"]`).should('have.value', '12899.07');

      cy.get(`[data-cy="totalPrice"]`).type('339.35');
      cy.get(`[data-cy="totalPrice"]`).should('have.value', '339.35');

      cy.get(`[data-cy="mrp"]`).type('766.47');
      cy.get(`[data-cy="mrp"]`).should('have.value', '766.47');

      cy.get(`[data-cy="tax"]`).type('1359.15');
      cy.get(`[data-cy="tax"]`).should('have.value', '1359.15');

      cy.get(`[data-cy="qtyText"]`).type('swiftly');
      cy.get(`[data-cy="qtyText"]`).should('have.value', 'swiftly');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T20:17');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T20:17');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T07:17');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T07:17');

      cy.get(`[data-cy="product"]`).select(1);
      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        cart = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', cartPageUrlPattern);
    });
  });
});
