import java.util.ArrayList;
import java.util.Random;


public class SimulatedAnnealing
{
    double coolingrate=0.1;
    double temperature=4000;
    int[][] conflictMatrix;
    int[] initialSolution;
    int iteration;
    int timeslotCount;
    int jumlahCourse;
    ArrayList<String> student;

    public SimulatedAnnealing(double temperature,double coolingrate,
                              int[][] conflictMatrix, int[] initialSolution, int iteration,
                              int timeslotCount, ArrayList<String> student)
    {
        this.coolingrate = coolingrate;
        this.conflictMatrix =conflictMatrix;
        this.initialSolution = initialSolution;
        this.iteration = iteration;
        this.timeslotCount = timeslotCount;
        this.student = student;

    }

    public int[] SATimeslot ()
    {
        //Membuat array SATimeslot yang berisi solusi awal (largest degree)
        int[] SATimeslot = new int[initialSolution.length];

        for(int i = 0; i<initialSolution.length; i++)
        {
            SATimeslot[i] = initialSolution[i];
        }

        //Menghitung penalty solusi awal
        double initialPenalty = Penalty.countPenalty(student, SATimeslot);
        double currentPenalty = Penalty.countPenalty(student, SATimeslot);
        double bestPenalty    = Penalty.countPenalty(student, SATimeslot);

        for	(int i=0; i < iteration; i++)
        {
            int[] tempTimeslot = new int[SATimeslot.length];
            for(int j = 0; j<SATimeslot.length; j++)
            {
                tempTimeslot[j] = SATimeslot[j];
            }
            //Memilih LLH secara random
            int randomLLH = 1 + (int)(Math.random() * ((5 - 1) + 1));
            LLH LLHRandom = new LLH(tempTimeslot,timeslotCount);
            switch (randomLLH) {
                case 1:
                    tempTimeslot = LLHRandom.move(); break;
                case 2:
                    tempTimeslot = LLHRandom.swap(); break;
                case 3:
                    tempTimeslot = LLHRandom.move2(); break;
                case 4:
                    tempTimeslot = LLHRandom.swap3(); break;
                case 5:
                    tempTimeslot = LLHRandom.move3(); break;
                default :
                    System.out.println(randomLLH + " is Out of Bound");
            }

            System.out.println("Suhu : " + temperature);

            double newPenalty = Penalty.countPenalty(student, tempTimeslot);

            if(isConflicted(tempTimeslot))
            {
                //Menambah penalti solusi sebanyak 1000000
                newPenalty = newPenalty + 1000000;
                //System.out.println(newPenalty);
            }

            else if (newPenalty<currentPenalty)
            {
                for(int j = 1; j<tempTimeslot.length; j++)
                {
                    SATimeslot[j] = tempTimeslot[j];
                }

                currentPenalty = newPenalty;
            }

            else if(newPenalty>currentPenalty && Math.exp(currentPenalty-newPenalty/temperature) > Math.random())
            {
                for(int j = 1; j<tempTimeslot.length; j++)
                {
                    SATimeslot[j] = tempTimeslot[j];
                }

                currentPenalty = newPenalty;

                temperature = temperature - coolingrate;
            }

            bestPenalty = currentPenalty;

            double deltaPenalty = ((initialPenalty-bestPenalty)/currentPenalty)*100;

            // print initial penalty
            System.out.println("\nInitial Penalty   : "+ initialPenalty);

            // print best penalty
            System.out.println("Best Penalty      : " + bestPenalty);

            // print improvement
            System.out.println("Delta Improvement : " + deltaPenalty + " % dari inisial solusi");

        }
        return SATimeslot;
    }

    public boolean isConflicted(int[] timeslot)
    {
        for(int i = 1; i<timeslot.length; i++){
            for(int j=i; j<timeslot.length; j++){
                if(timeslot[i] == timeslot[j] && i!=j){
                    int course1 = i;
                    int course2 = j;
                    if(this.conflictMatrix[course1][course2]==1){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*private static double acceptanceProbability(double currentPenalty,
                double newPenalty, double temperature)
    {
	// If the new solution is better, accept it
	if (newPenalty < currentPenalty)
        {
            return 1.0;
        }

		// If the new solution is worse, calculate an acceptance probability
	return Math.exp((currentPenalty - newPenalty) / temperature);
    }*/
}
