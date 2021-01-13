import java.util.ArrayList;

public class HillClimbing {
    int[][] conflictMatrix;
    int[] initialSolution;
    int iteration;
    int timeslotCount;
    int jumlahCourse;
    ArrayList<String> student;

    public HillClimbing(int[][] conflictMatrix, int[] initialSolution, int iteration, int timeslotCount, ArrayList<String> student)
    {
        this.conflictMatrix = conflictMatrix;
        this.initialSolution = initialSolution;
        this.iteration = iteration;
        this.timeslotCount = timeslotCount;
        this.student = student;
    }

    public int[] optimizeTimeslot (){
        //Membuat array optimizedTimeslot yang berisi solusi awal (largest degree)
        int[] optimizedTimeslot = new int[initialSolution.length];
        for(int i = 0; i<initialSolution.length; i++){
            optimizedTimeslot[i] = initialSolution[i];
        }
        //Menghitung penalty solusi awal
        double currentPenalty = Penalty.countPenalty(student, optimizedTimeslot);

        //Melakukan iterasi hillclimbing sebanyak jumlah iterasi yang diinput
        for (int i =0; i < iteration; i++){
            //Membuat array newTimeslot untuk menyimpan solusi sementara
            int[] newTimeslot = new int[optimizedTimeslot.length];
            for(int j = 0; j<optimizedTimeslot.length; j++){
                newTimeslot[j] = optimizedTimeslot[j];
            }

            //Mengenerate randomcourse dan randomtimeslot
            int randomCourse = (int)(Math.random()*initialSolution.length);
            //int randomCourse2 = (int)(Math.random()*initialSolution.length);
            int randomTimeslot = (int) (Math.random()*timeslotCount);

            //Memilih course secara random dan mengubah timeslotnya secara random
            newTimeslot[randomCourse] = randomTimeslot;
            //Menghitung penalty solusi timeslot baru
            double newPenalty = Penalty.countPenalty(student, newTimeslot);

            //Mengecek apakah timeslot course yang sudah diubah secara random konflik dengan course lain pada timeslot yang sama
            if(isConflicted(newTimeslot, randomCourse)){
                //Menambah penalti solusi sebanyak 1000000
                newPenalty += 1000000;
                //System.out.println(newPenalty);
            }

            //Mengecek apakah penalti solusi baru < penalti solusi lama
            else if(newPenalty<currentPenalty){
                System.out.println(newPenalty);
                System.out.println("Course "+ (randomCourse+1) + " berpindah dari timeslot " +optimizedTimeslot[randomCourse]
                        + " ke timeslot " + randomTimeslot);
                //Jika lebih kecil maka solusi awal diubah sesuai solusi baru
                optimizedTimeslot[randomCourse] = randomTimeslot;
                //Penalti lama diganti berdasarkan penalti baru
                currentPenalty = newPenalty;
            }

            //Jika tidak konflik namun penalti baru lebih jelek maka tidak melakukan apa-apa
            else{
                //System.out.println(newPenalty);
                //System.out.println("New Penalty is worse");
            }
        }

        for (int i = 0; i < this.jumlahCourse; i++)
            System.out.println("Timeslot untuk course "+ (i+1) + " adalah timeslot " + optimizedTimeslot[i]);

        System.out.println("Penalti baru = "+currentPenalty);
        return optimizedTimeslot;
    }

    public boolean isConflicted(int[] timeslot, int course){
        for(int i = 0; i<timeslot.length; i++){
            if(timeslot[course] == timeslot[i]){
                if(this.conflictMatrix[course][i]==1){
//                        System.out.println(course1 + " dan " + course2 + " konflik");
                    return true;
                }
            }
        }
        return false;
    }


}
