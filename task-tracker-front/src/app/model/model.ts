import { TaskTracker } from './tasktracker';

export class DisplayModel {
  empId: string;
  name: string;
  message:string;
  totalDuration: number = 0;
  tasks: TaskTracker[] = new Array();
}
