/**
 * Copyright (C) 2016 LibRec
<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
<<<<<<< HEAD
 * <p>
=======
 * 
>>>>>>> master
=======
 * 
>>>>>>> last minute commit.
 * This file is part of LibRec.
 * LibRec is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
<<<<<<< HEAD
 * <p>
=======
 *
>>>>>>> master
=======
 *
>>>>>>> last minute commit.
 * LibRec is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
<<<<<<< HEAD
 * <p>
=======
 *
>>>>>>> master
=======
 *
>>>>>>> last minute commit.
 * You should have received a copy of the GNU General Public License
 * along with LibRec. If not, see <http://www.gnu.org/licenses/>.
 */
package net.librec.similarity;

import java.util.List;

/**
 * Calculate Mean Squared Difference (MSD) similarity proposed by Shardanand and Maes [1995]:
<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
<<<<<<< HEAD
 <i>Social information filtering: Algorithms for automating "word of mouth"</i>
 *
 * Mean Squared Difference (MSD) Similarity
 *
=======
=======
>>>>>>> last minute commit.
	 <i>Social information filtering: Algorithms for automating "word of mouth"</i>
* 
* Mean Squared Difference (MSD) Similarity
* 
<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
>>>>>>> master
=======
>>>>>>> last minute commit.
 * @author zhanghaidong
 *
 */
public class MSDSimilarity extends AbstractRecommenderSimilarity {
<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
<<<<<<< HEAD

    /**
     *
     * @param thisList
     *
     * @param thatList
     *
     * @return MSD similarity
     **/
    protected double getSimilarity(List<? extends Number> thisList, List<? extends Number> thatList) {
        if (thisList == null || thatList == null || thisList.size() < 1 || thatList.size() < 1 || thisList.size() != thatList.size()) {
            return Double.NaN;
        }

        double sum = 0.0;

        for (int i = 0; i < thisList.size(); i++) {
            double thisValue = thisList.get(i).doubleValue();
            double thatValue = thatList.get(i).doubleValue();

            sum += Math.pow(thisValue - thatValue, 2);
        }

        double sim = thisList.size() / sum;
        if (Double.isInfinite(sim))
            sim = 1.0;

        return sim;
    }
=======
=======
>>>>>>> last minute commit.
	
	/**
	 * 
	 * @param thisList
	 *            
	 * @param thatList
	 * 
	 * @return MSD similarity
	 **/
	protected double getSimilarity(List<? extends Number> thisList, List<? extends Number> thatList) {
		if (thisList == null || thatList == null || thisList.size() < 1 || thatList.size() < 1 || thisList.size() != thatList.size()) {
			return Double.NaN;
		}
		
		double sum = 0.0;

		for (int i = 0; i < thisList.size(); i++) {
			double thisValue = thisList.get(i).doubleValue();
			double thatValue = thatList.get(i).doubleValue();

			sum += Math.pow(thisValue - thatValue, 2);
		}

		double sim = thisList.size() / sum;
		if (Double.isInfinite(sim))
			sim = 1.0;

		return sim;
	}
<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
>>>>>>> master
=======
>>>>>>> last minute commit.

}