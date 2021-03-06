package model.param;

import java.util.Random;

import javax.management.RuntimeErrorException;

import util.MyArray;
import util.Stats;

public abstract class MultinomialBase {
	//x,y == P(x given y)
	int x,y;
	public double[][] count;
	public double[][] oldCount;
	
	public MultinomialBase(int x, int y) {
		this.x = x; this.y = y;
		count = new double[x][y];
	}
	
	public double get(int x, int y) {
		return count[x][y];
	}
	
	public void set(int x, int y, double value) {
		count[x][y] = value;
	}
	
	public void addToCounts(int x, int y, double value) {
		count[x][y] += value;
	}
	
	public void cloneFrom(MultinomialBase source) {
		for(int i=0; i<y; i++) {
			for(int j=0; j<x; j++) {
				count[j][i] = source.count[j][i];
			}
		}
	}
	
	public boolean equalsExact(MultinomialBase other) {
		boolean result = true;
		for(int i=0; i<y; i++) {
			for(int j=0; j<x; j++) {
				if(count[j][i] == other.get(j,i)) {
					result = false;
				}
			}
		}
		return result;
	}
	
	public boolean equalsApprox(MultinomialBase other) {
		double precision = 1e-200;
		boolean result = true;
		for(int i=0; i<y; i++) {
			for(int j=0; j<x; j++) {
				if(Math.abs(count[j][i] - other.get(j,i)) > precision) {
					result = false;
				}
			}
		}
		return result;
	}
	
	public void cloneWeightedFrom(MultinomialBase source, double weight) {
		if(oldCount == null) {
			oldCount = MyArray.getCloneOfMatrix(source.count);
		}
		for(int i=0; i<y; i++) {
			for(int j=0; j<x; j++) {
				//weighted expected counts
				count[j][i] = weight * source.count[j][i] + (1-weight) * oldCount[j][i];
			}
		} 
		//store the new expected counts for next iteration
		oldCount = MyArray.getCloneOfMatrix(count);		
	}
	
	/*
	 * adds the counts from other multinomials
	 */
	public void addFromOtherMultinomial(MultinomialBase other) {
		MyArray.addToMatrix(this.count, other.count);
	}
	public abstract void initializeRandom(Random r);
	public abstract void smooth();
	public abstract void normalize();
	public abstract void normalize(MultinomialBase other);
	public abstract void checkDistribution();
	public abstract void printDistribution();	
}
