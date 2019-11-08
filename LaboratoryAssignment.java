import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LaboratoryAssignment{

	private static int studentNum = 0;
	private static int teacherNum = 0;
	private final static int ACCEPT = 3;

	public static void main(String[] args) throws IOException{
		int student[][] = null;
		int teacher[][] = null;
		int studentWish[];
		int statusInLaboratory[][];
		String path = "test.txt";

		try{
			File file = new File(path);

			if(file.exists()){
				Scanner sc = new Scanner(file);
				if(sc.hasNext()) {
					studentNum = Integer.parseInt(sc.next());
				}
				if(sc.hasNext()) {
					teacherNum = Integer.parseInt(sc.next());
				}

				if(studentNum / teacherNum > ACCEPT){
					System.out.println("ACCEPT is not correct");
					System.exit(1);
				}

				student = new int[studentNum][teacherNum];
				teacher = new int[teacherNum][studentNum];

				for(int i = 0; i < studentNum; i++){
					for(int j = 0; j < teacherNum; j++){
						if(sc.hasNext()){
							student[i][j] = Integer.parseInt(sc.next());
						}
						else{
							System.out.println("Data of student" + i + " is not correct");
							System.exit(1);
						}
					}
				}

				for(int i = 0; i < teacherNum; i++){
					for(int j = 0; j < studentNum; j++){
						if(sc.hasNext()){
							teacher[i][j] = Integer.parseInt(sc.next());
						}else{
							System.out.println("Data of teacher" + i + " is not correct");
							System.exit(1);
						}
					}
				}
			}else{
				System.out.println("File is not exist");
				System.exit(1);
			}
		}catch(IOException e){
			e.printStackTrace();
		}

        studentWish = new int[studentNum];
        statusInLaboratory = new int[teacherNum][ACCEPT];

        for(int i = 0; i < studentNum; i++){
        	studentWish[i] = student[i][0];
        }

        for(int i = 0; i < teacherNum; i++){
        	for(int j = 0; j < ACCEPT; j++){
        		statusInLaboratory[i][j] = -1;
        	}
        }

        int studentIndex = 0;
        while((studentIndex = notDeterminedStudentIndex(studentWish)) >= 0){
        	int wish = studentWish[studentIndex];

        	if (statusInLaboratory[wish][ACCEPT-1] == -1){
				statusInLaboratory[wish][ACCEPT-1] = studentIndex;
				studentWish[studentIndex] = -1;
			}else{
				int lastPerson = statusInLaboratory[wish][ACCEPT-1];
				int selectedStudent = teacherWishStudent(teacher, wish, studentIndex, lastPerson);

				if (selectedStudent == studentIndex){
					statusInLaboratory[wish][ACCEPT-1] = studentIndex;
					studentWish[studentIndex] = -1;
					changeStudentWish(student, studentWish, lastPerson);
				}else{
					changeStudentWish(student, studentWish, studentIndex);
				}
			}
			sortLaboratory(teacher, statusInLaboratory, wish);
		}
        for(int i = 0; i < teacherNum; i++) {
        	System.out.print("Laboratory" + i + " has ");
        	for(int j = 0; j < ACCEPT; j++) {
        		System.out.print("student" + statusInLaboratory[i][j] + ",");
        	}
        	System.out.println();
        }
	}

	private static int notDeterminedStudentIndex(int studentWish[]){
		for(int i = 0; i < studentNum; i++){
			if(studentWish[i] >= 0){
				return i;
			}
		}
		return -1;
	}

	private static int teacherWishStudent(int teacher[][], int targetTeacherNum, int newStudent, int lastStudent){
		for(int j = 0; j < studentNum; j++){
			if(teacher[targetTeacherNum][j] == newStudent){
				return newStudent;
			}
			else if(teacher[targetTeacherNum][j] == lastStudent){
				return lastStudent;
			}
		}
		System.out.println("Data of teacher's wish is not correct");
		System.exit(1);
		return 0;
	}

	private static void changeStudentWish(int student[][], int studentWish[], int studentIndex){
		for(int i = 0;i < teacherNum - 1; i++){
			if(student[studentIndex][i] >= 0){
				student[studentIndex][i] = -1;
				studentWish[studentIndex] = student[studentIndex][i+1];
				break;
			}
		}
	}

	private static void sortLaboratory(int teacher[][], int statusInLaboratory[][], int teacherIndex){
		int[] status = new int[ACCEPT];
		for(int i = 0;i < ACCEPT; i++){
			status[i] = -1;
		}
		int statusIndex = 0;
		for(int i = 0; i < studentNum; i++){
			for(int j = 0; j < ACCEPT; j++){
				if(statusInLaboratory[teacherIndex][j] == teacher[teacherIndex][i]){
					status[statusIndex++] = statusInLaboratory[teacherIndex][j];
					break;
				}
			}
		}
		System.arraycopy(status, 0, statusInLaboratory[teacherIndex], 0, ACCEPT);
	}
}
