
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Recommented {
	public static void main(String[] args) throws IOException {
		Recommented R = new Recommented();
		String[][] item = new String[1000][1000];
		int hang = 5; // số hàng ma trận (item)
		int cot = 7; // số cột ma trận (user)
		File file = new File("input.txt");
		if (file.exists()) {
			FileReader rf = null;
			BufferedReader bf = null;
			try {
				rf = new FileReader(file);
				bf = new BufferedReader(rf);
				String line = "";
				int i = 0;
				System.out.println("Matrix X:");
				while ((line = bf.readLine()) != null) {
					item[i] = line.split("\\s");
					System.out.println(line);
					i++;
				}
				System.out.println("-------------");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Chuẩn hóa ma trận X");
		R.Normalized(item, hang, cot);
		String[][] itemS = new String[hang][cot];
		for(int i = 0; i < hang; i++){
			for(int j = 0; j < cot; j++){
				itemS[i][j] = item[i][j];
			}
		}
		for (int h = 0; h < hang; h++) {
			for (int j = 0; j < cot; j++) {
				if (item[h][j].equals("0")) {
					ArrayList<Similarity> similarity = new ArrayList<>();
					System.out.println("Item cần dự đoán: item[" + h + "][" + j + "] : " + item[h][j]);
					for (int k = 0; k < hang; k++) {
						System.out.println("item[" + k + "][" + j + "] = " + item[k][j]);
						if (!item[k][j].equals("0") && h != k) {
							similarity.add(new Similarity(k,(float) Math.round(R.CosineSimilarity(item[h], item[k]) * 100) / 100));
							System.out.println("Cosine(" + h + "," + k + ") = "
									+ (float) Math.round(R.CosineSimilarity(item[h], item[k]) * 100) / 100);
						}
					}
					Collections.sort(similarity, new CosineComparator()); // sắp xếp theo chiều Similarity giảm dần để lấy số nearest neighbors
					//chọn số nearest neighbors k = 2
					int k = 2;
					float T = 0;
					float M = 0;
					for(int i = 0; i < k; i++){
						System.out.println("hàng : "+similarity.get(i).getItem()+" cosine: "+similarity.get(i).getCosine());
						T += (float) (similarity.get(i).getCosine() * Float.parseFloat(item[similarity.get(i).getItem()][j]));
						M += Math.abs(similarity.get(i).getCosine());
					}
					float DuDoan = (float) Math.round((T / M) * 100) / 100;
					itemS[h][j] = Float.toString(DuDoan);
					System.out.println("Dự đoán : " + DuDoan);
					System.out.println("----------");
				}
			}
		}
		R.CreateMatrix(itemS, hang, cot);
		System.out.println("Matrix Y : ");
		for(int i = 0; i < hang; i++){
			for(int j = 0; j < cot; j++){
				System.out.print(itemS[i][j]+" ");
			}
			System.out.println();
		}
		R.Write(itemS, hang, cot);
	}

	public void Normalized(String[][] item, int hang, int cot) {
		float[] P = new float[hang];
		for (int j = 0; j < hang; j++) {
			int dem = 0;
			int S = 0;
			for (int k = 0; k < cot; k++) {
				if (Integer.parseInt(item[j][k]) != -1) {
					dem++;
					S += Integer.parseInt(item[j][k]);
				}
			}
			P[j] = (float) S / dem;
			P[j] = (float) Math.round(P[j] * 100) / 100;
			for (int k = 0; k < cot; k++) {
				if (Integer.parseInt(item[j][k]) != -1) {
					item[j][k] = Float.toString((float) Math.round((Float.parseFloat(item[j][k]) - P[j]) * 100) / 100);
					System.out.print(item[j][k] + " ");
				} else {
					item[j][k] = "0";
					System.out.print(item[j][k] + " ");
				}
			}
			System.out.println();
		}

	}
	
	public void CreateMatrix(String[][] itemS, int hang, int cot) throws IOException {
		String[][] item = new String[1000][1000];
		File file = new File("input.txt");
		if (file.exists()) {
			FileReader rf = null;
			BufferedReader bf = null;
			try {
				rf = new FileReader(file);
				bf = new BufferedReader(rf);
				String line = "";
				int i = 0;
				while ((line = bf.readLine()) != null) {
					item[i] = line.split("\\s");
					i++;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		float[] P = new float[hang];
		for (int j = 0; j < hang; j++) {
			int dem = 0;
			int S = 0;
			for (int k = 0; k < cot; k++) {
				if (Float.parseFloat(item[j][k]) != -1) {
					dem++;
					S += Float.parseFloat(item[j][k]);
				}
			}
			P[j] = (float) S / dem;
			P[j] = (float) Math.round(P[j] * 100) / 100;
			for (int k = 0; k < cot; k++) {
				itemS[j][k] = Float.toString((float) Math.round((Float.parseFloat(itemS[j][k]) + P[j]) * 100) / 100);
			}
		}

	}

	public float CosineSimilarity(String[] a, String[] b) {
		float kq = 0;
		float ab = 0, a2 = 0, b2 = 0;
		for (int i = 0; i < a.length; i++) {
			ab += Float.parseFloat(a[i]) * Float.parseFloat(b[i]);
			a2 += Float.parseFloat(a[i]) * Float.parseFloat(a[i]);
			b2 += Float.parseFloat(b[i]) * Float.parseFloat(b[i]);
		}
		kq = (float) (ab / (Math.sqrt(a2) * Math.sqrt(b2)));
		return kq;
	}

	public void Write(String[][] item, int hang, int cot){
		 File file = new File("output.txt");
		 if(!file.exists()){
			 try {
				 file.createNewFile();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
		 }
		 FileWriter fw = null;
		 BufferedWriter bw = null;
		 try {
			 fw = new FileWriter(file);
			 bw = new BufferedWriter(fw);
			 //write data
			 for(int h = 0; h < hang; h++){
				 for(int c = 0; c < cot; c++){
					 bw.write(item[h][c]+" ");
				 }
				 bw.newLine();
			 }
			 bw.close();
			 fw.close();
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
	 }
}
