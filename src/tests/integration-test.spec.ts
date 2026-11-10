import { ComponentFixture } from '@angular/core/testing';
import { RegisterPageObject } from './pages/register.page';
import { TestSetupUtil } from './utils/test-setup.util';
import { HttpTestUtil } from './utils/http-test.util';
import { RouterTestUtil } from './utils/router.util';

type TicketType = 'standard' | 'vip';

type RegistrationData = {
  name: string;
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
    const registrationData: RegistrationData = {
      name: 'Fato',
      email: 'fato@mail.com',
      ticketType: 'vip',
      quantity: 2,
      price: 49,
    };

    registerPage.setName(registrationData.name);
    await registerPage.setEmail(registrationData.email);
    registerPage.selectTicketType(registrationData.ticketType);
    registerPage.setQuantity(registrationData.quantity);
    registerPage.acceptTerms();

    expect(registerPage.getTotalPrice()).toBe(registrationData.price);

    HttpTestUtil.verify();
  });
});
