import { Employee } from './employee';

export class TaskTracker {
  taskId: number;
  employee: Employee;
  taskDate: Date;
  taskName: string;
  additionalDetails: string;
  startTime: Date;
  endTime: Date;
  duration: number;
}
