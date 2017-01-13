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
package net.librec.eval.ranking;

import net.librec.eval.AbstractRecommenderEvaluator;
import net.librec.math.algorithm.Maths;
import net.librec.math.structure.SparseMatrix;
import net.librec.recommender.item.ItemEntry;
import net.librec.recommender.item.RecommendedList;

<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
<<<<<<< HEAD
=======
import java.util.ArrayList;
>>>>>>> master
=======
import java.util.ArrayList;
>>>>>>> last minute commit.
import java.util.List;
import java.util.Set;

/**
 * NormalizedDCGEvaluator
<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
<<<<<<< HEAD
 *
=======
 * 
>>>>>>> master
=======
 * 
>>>>>>> last minute commit.
 * @author KEVIN
 */
public class NormalizedDCGEvaluator extends AbstractRecommenderEvaluator {

<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
<<<<<<< HEAD
    public double evaluate(SparseMatrix testMatrix, RecommendedList recommendedList) {
        if (testMatrix.size() == 0) {
            return 0.0;
        }

        double nDCG = 0.0;

        int numUsers = testMatrix.numRows();
        int nonZeroNumUsers = 0;
        for (int userID = 0; userID < numUsers; userID++) {
            double dcg = 0.0;
            double idcg = 0.0;

            Set<Integer> testListByUser = testMatrix.getColumnsSet(userID);
            if (testListByUser.size() > 0) {
                List<ItemEntry<Integer, Double>> recommendListByUser = recommendedList.getItemIdxListByUserIdx(userID);

                // calculate the IDCG
                int numItemsInTestList = testListByUser.size();
                for (int i = 0; i < numItemsInTestList; i++) {
                    idcg += 1 / Maths.log(i + 2.0, 2);
                }

                // calculate DCG
                int numItemsInRecommendedList = recommendListByUser.size();
                for (int i = 0; i < numItemsInRecommendedList; i++) {
                    int itemID = recommendListByUser.get(i).getKey();
                    if (!testListByUser.contains(itemID)) {
                        continue;
                    }

                    int rank = i + 1;
                    dcg += 1 / Maths.log(rank + 1, 2);
                }

                nDCG += dcg / idcg;
                nonZeroNumUsers++;
            }
        }

        return nDCG / nonZeroNumUsers;
    }
=======
=======
>>>>>>> last minute commit.
	public double evaluate(SparseMatrix testMatrix, RecommendedList recommendedList) {
		if (testMatrix.size() == 0) {
			return 0.0;
		}

		double nDCG = 0.0;

		int numUsers = testMatrix.numRows();
		for (int userID = 0; userID < numUsers; userID++) {
			double dcg = 0.0;
			double idcg = 0.0;

			Set<Integer> testListByUser = testMatrix.getColumnsSet(userID);
			List<ItemEntry<Integer, Double>> recommendListByUser = recommendedList.getItemIdxListByUserIdx(userID);

			// calculate the IDCG
			int numItemsInTestList = testListByUser.size();
			for (int i = 0; i < numItemsInTestList; i++) {
				idcg += 1 / Maths.log(i + 2.0, 2);
			}

			// calculate DCG
			int numItemsInRecommendedList = recommendListByUser.size();
			for (int i = 0; i < numItemsInRecommendedList; i++) {
				int itemID = recommendListByUser.get(i).getKey();
				if (!testListByUser.contains(itemID)) {
					continue;
				}

				int rank = i + 1;
				dcg += 1 / Maths.log(rank + 1, 2);
			}

			nDCG += dcg / idcg;
		}

		return nDCG / numUsers;
	}
<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
>>>>>>> master
=======
>>>>>>> last minute commit.

}