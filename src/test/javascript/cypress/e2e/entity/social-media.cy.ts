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

describe('SocialMedia e2e test', () => {
  const socialMediaPageUrl = '/social-media';
  const socialMediaPageUrlPattern = new RegExp('/social-media(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const socialMediaSample = { title: 'if outside till', image: 'counterpart drafty', url: 'https://periodic-mussel.com/' };

  let socialMedia;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/social-medias+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/social-medias').as('postEntityRequest');
    cy.intercept('DELETE', '/api/social-medias/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (socialMedia) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/social-medias/${socialMedia.id}`,
      }).then(() => {
        socialMedia = undefined;
      });
    }
  });

  it('SocialMedias menu should load SocialMedias page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('social-media');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SocialMedia').should('exist');
    cy.url().should('match', socialMediaPageUrlPattern);
  });

  describe('SocialMedia page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(socialMediaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SocialMedia page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/social-media/new$'));
        cy.getEntityCreateUpdateHeading('SocialMedia');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', socialMediaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/social-medias',
          body: socialMediaSample,
        }).then(({ body }) => {
          socialMedia = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/social-medias+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/social-medias?page=0&size=20>; rel="last",<http://localhost/api/social-medias?page=0&size=20>; rel="first"',
              },
              body: [socialMedia],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(socialMediaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SocialMedia page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('socialMedia');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', socialMediaPageUrlPattern);
      });

      it('edit button click should load edit SocialMedia page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SocialMedia');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', socialMediaPageUrlPattern);
      });

      it('edit button click should load edit SocialMedia page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SocialMedia');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', socialMediaPageUrlPattern);
      });

      it('last delete button click should delete instance of SocialMedia', () => {
        cy.intercept('GET', '/api/social-medias/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('socialMedia').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', socialMediaPageUrlPattern);

        socialMedia = undefined;
      });
    });
  });

  describe('new SocialMedia page', () => {
    beforeEach(() => {
      cy.visit(`${socialMediaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SocialMedia');
    });

    it('should create an instance of SocialMedia', () => {
      cy.get(`[data-cy="title"]`).type('yieldingly after');
      cy.get(`[data-cy="title"]`).should('have.value', 'yieldingly after');

      cy.get(`[data-cy="image"]`).type('uh-huh apud e-reader');
      cy.get(`[data-cy="image"]`).should('have.value', 'uh-huh apud e-reader');

      cy.get(`[data-cy="url"]`).type('https://rotating-maniac.biz/');
      cy.get(`[data-cy="url"]`).should('have.value', 'https://rotating-maniac.biz/');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T13:29');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T13:29');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T11:49');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T11:49');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        socialMedia = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', socialMediaPageUrlPattern);
    });
  });
});
