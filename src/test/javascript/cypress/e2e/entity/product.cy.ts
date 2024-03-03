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

describe('Product e2e test', () => {
  const productPageUrl = '/product';
  const productPageUrlPattern = new RegExp('/product(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const productSample = {"title":"favorable","qtyText":"brightly crown","price":14991.64,"tax":22270.36,"mrp":14781.29,"subscription":false,"isActive":false};

  let product;
  // let subCat;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/sub-cats',
      body: {"title":"heavily trusty","createdAt":"2024-03-02T10:44:18.875Z","updatedAt":"2024-03-03T06:11:40.138Z","isActive":true},
    }).then(({ body }) => {
      subCat = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/products+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/products').as('postEntityRequest');
    cy.intercept('DELETE', '/api/products/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/sub-cats', {
      statusCode: 200,
      body: [subCat],
    });

    cy.intercept('GET', '/api/carts', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/orders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/subscribed-orders', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (product) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/products/${product.id}`,
      }).then(() => {
        product = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (subCat) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sub-cats/${subCat.id}`,
      }).then(() => {
        subCat = undefined;
      });
    }
  });
   */

  it('Products menu should load Products page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Product').should('exist');
    cy.url().should('match', productPageUrlPattern);
  });

  describe('Product page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Product page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product/new$'));
        cy.getEntityCreateUpdateHeading('Product');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', productPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/products',
          body: {
            ...productSample,
            subCat: subCat,
          },
        }).then(({ body }) => {
          product = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/products+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/products?page=0&size=20>; rel="last",<http://localhost/api/products?page=0&size=20>; rel="first"',
              },
              body: [product],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(productPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(productPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Product page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('product');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', productPageUrlPattern);
      });

      it('edit button click should load edit Product page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Product');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', productPageUrlPattern);
      });

      it('edit button click should load edit Product page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Product');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', productPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Product', () => {
        cy.intercept('GET', '/api/products/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('product').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', productPageUrlPattern);

        product = undefined;
      });
    });
  });

  describe('new Product page', () => {
    beforeEach(() => {
      cy.visit(`${productPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Product');
    });

    it.skip('should create an instance of Product', () => {
      cy.get(`[data-cy="title"]`).type('than furthermore');
      cy.get(`[data-cy="title"]`).should('have.value', 'than furthermore');

      cy.get(`[data-cy="qtyText"]`).type('which');
      cy.get(`[data-cy="qtyText"]`).should('have.value', 'which');

      cy.get(`[data-cy="stockQty"]`).type('32644');
      cy.get(`[data-cy="stockQty"]`).should('have.value', '32644');

      cy.get(`[data-cy="price"]`).type('28031.46');
      cy.get(`[data-cy="price"]`).should('have.value', '28031.46');

      cy.get(`[data-cy="tax"]`).type('6193.94');
      cy.get(`[data-cy="tax"]`).should('have.value', '6193.94');

      cy.get(`[data-cy="mrp"]`).type('8303.96');
      cy.get(`[data-cy="mrp"]`).should('have.value', '8303.96');

      cy.get(`[data-cy="offerText"]`).type('whoever among');
      cy.get(`[data-cy="offerText"]`).should('have.value', 'whoever among');

      cy.get(`[data-cy="description"]`).type('gah sympathetically');
      cy.get(`[data-cy="description"]`).should('have.value', 'gah sympathetically');

      cy.get(`[data-cy="disclaimer"]`).type('whenever');
      cy.get(`[data-cy="disclaimer"]`).should('have.value', 'whenever');

      cy.get(`[data-cy="subscription"]`).should('not.be.checked');
      cy.get(`[data-cy="subscription"]`).click();
      cy.get(`[data-cy="subscription"]`).should('be.checked');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T09:43');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T09:43');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T16:05');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T16:05');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="subCat"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        product = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', productPageUrlPattern);
    });
  });
});
