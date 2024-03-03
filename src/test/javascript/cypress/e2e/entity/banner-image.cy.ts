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

describe('BannerImage e2e test', () => {
  const bannerImagePageUrl = '/banner-image';
  const bannerImagePageUrlPattern = new RegExp('/banner-image(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const bannerImageSample = { image: 'wildly', imageType: true };

  let bannerImage;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/banner-images+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/banner-images').as('postEntityRequest');
    cy.intercept('DELETE', '/api/banner-images/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (bannerImage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/banner-images/${bannerImage.id}`,
      }).then(() => {
        bannerImage = undefined;
      });
    }
  });

  it('BannerImages menu should load BannerImages page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('banner-image');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BannerImage').should('exist');
    cy.url().should('match', bannerImagePageUrlPattern);
  });

  describe('BannerImage page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(bannerImagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BannerImage page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/banner-image/new$'));
        cy.getEntityCreateUpdateHeading('BannerImage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bannerImagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/banner-images',
          body: bannerImageSample,
        }).then(({ body }) => {
          bannerImage = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/banner-images+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/banner-images?page=0&size=20>; rel="last",<http://localhost/api/banner-images?page=0&size=20>; rel="first"',
              },
              body: [bannerImage],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(bannerImagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BannerImage page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('bannerImage');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bannerImagePageUrlPattern);
      });

      it('edit button click should load edit BannerImage page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BannerImage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bannerImagePageUrlPattern);
      });

      it('edit button click should load edit BannerImage page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BannerImage');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bannerImagePageUrlPattern);
      });

      it('last delete button click should delete instance of BannerImage', () => {
        cy.intercept('GET', '/api/banner-images/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('bannerImage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bannerImagePageUrlPattern);

        bannerImage = undefined;
      });
    });
  });

  describe('new BannerImage page', () => {
    beforeEach(() => {
      cy.visit(`${bannerImagePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BannerImage');
    });

    it('should create an instance of BannerImage', () => {
      cy.get(`[data-cy="image"]`).type('recent maximise');
      cy.get(`[data-cy="image"]`).should('have.value', 'recent maximise');

      cy.get(`[data-cy="imageType"]`).should('not.be.checked');
      cy.get(`[data-cy="imageType"]`).click();
      cy.get(`[data-cy="imageType"]`).should('be.checked');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-03T06:44');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-03T06:44');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T18:35');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T18:35');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        bannerImage = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', bannerImagePageUrlPattern);
    });
  });
});
