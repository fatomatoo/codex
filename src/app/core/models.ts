export type TicketType = 'standard' | 'vip';

export interface NewRegistration {
  name: string;
  email: string;
  ticketType: TicketType;
  quantity: number;
}

export interface Registration extends NewRegistration {
  id: string;
  total: number;
  createdAt: string;
}
