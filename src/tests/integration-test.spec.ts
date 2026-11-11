import { ComponentFixture } from '@angular/core/testing';
import { RegisterPageObject } from './pages/register.page';
import { TestSetupUtil } from './utils/test-setup.util';
import { HttpTestUtil } from './utils/http-test.util';
import { RouterTestUtil } from './utils/router.util';
import { page } from 'vitest/browser';

type TicketType = 'standard' | 'vip';

type RegistrationData = {
  email: string;
  ticketType: TicketType;
  quantity: number;
  price: number;
};

describe('Registration Form', () => {
  let registerPage: RegisterPageObject;
  let fixture: ComponentFixture<unknown>;

  beforeEach(async () => {
    TestSetupUtil.setupTestEnvironment();

    fixture = await RouterTestUtil.navigateToRegister();
    registerPage = new RegisterPageObject(fixture);
  });

  it('fills form and validates price VIP without discount', async () => {
    const registrationTestData: RegistrationData = {
      email: 'fato@mail.com',
      ticketType: 'vip',
      quantity: 2,
      price: 198,
    };

    registerPage.setEmail(registrationTestData.email);
    registerPage.selectTicketType(registrationTestData.ticketType);
    registerPage.setQuantity(registrationTestData.quantity);
    registerPage.acceptTerms();
    await fixture.whenStable();

    expect(registerPage.getTotalPrice()).toBe(registrationTestData.price);

    registerPage.submit();
    HttpTestUtil.mockCreateRegistration({ id: '1', ...registrationTestData });

    HttpTestUtil.verify();
  });
});
