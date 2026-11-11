export type TicketType = 'standard' | 'vip';

export interface NewRegistration {
  email: string;
  ticketType: TicketType;
  quantity: number;
}

export interface Registration extends NewRegistration {
  id: string;
}
