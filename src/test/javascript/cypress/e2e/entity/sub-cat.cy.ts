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

describe('SubCat e2e test', () => {
  const subCatPageUrl = '/sub-cat';
  const subCatPageUrlPattern = new RegExp('/sub-cat(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const subCatSample = { title: 'comportment strip', isActive: false };

  let subCat;
  let cat;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/cats',
      body: { title: 'ick', createdAt: '2024-03-02T19:37:31.377Z', updatedAt: '2024-03-02T13:15:00.798Z', isActive: false },
    }).then(({ body }) => {
      cat = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/sub-cats+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sub-cats').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sub-cats/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/cats', {
      statusCode: 200,
      body: [cat],
    });

    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [],
    });
  });

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

  afterEach(() => {
    if (cat) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cats/${cat.id}`,
      }).then(() => {
        cat = undefined;
      });
    }
  });

  it('SubCats menu should load SubCats page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('sub-cat');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SubCat').should('exist');
    cy.url().should('match', subCatPageUrlPattern);
  });

  describe('SubCat page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(subCatPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SubCat page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/sub-cat/new$'));
        cy.getEntityCreateUpdateHeading('SubCat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subCatPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sub-cats',
          body: {
            ...subCatSample,
            cat: cat,
          },
        }).then(({ body }) => {
          subCat = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sub-cats+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/sub-cats?page=0&size=20>; rel="last",<http://localhost/api/sub-cats?page=0&size=20>; rel="first"',
              },
              body: [subCat],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(subCatPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SubCat page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('subCat');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subCatPageUrlPattern);
      });

      it('edit button click should load edit SubCat page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubCat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subCatPageUrlPattern);
      });

      it('edit button click should load edit SubCat page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubCat');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subCatPageUrlPattern);
      });

      it('last delete button click should delete instance of SubCat', () => {
        cy.intercept('GET', '/api/sub-cats/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('subCat').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subCatPageUrlPattern);

        subCat = undefined;
      });
    });
  });

  describe('new SubCat page', () => {
    beforeEach(() => {
      cy.visit(`${subCatPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SubCat');
    });

    it('should create an instance of SubCat', () => {
      cy.get(`[data-cy="title"]`).type('virus per demanding');
      cy.get(`[data-cy="title"]`).should('have.value', 'virus per demanding');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T08:55');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T08:55');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T14:03');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T14:03');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="cat"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        subCat = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', subCatPageUrlPattern);
    });
  });
});
