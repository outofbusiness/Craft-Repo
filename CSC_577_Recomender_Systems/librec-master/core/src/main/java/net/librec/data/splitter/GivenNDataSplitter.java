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
package net.librec.data.splitter;

<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
<<<<<<< HEAD
=======
=======
>>>>>>> last minute commit.
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
>>>>>>> master
=======
>>>>>>> last minute commit.
import net.librec.common.LibrecException;
import net.librec.conf.Configuration;
import net.librec.data.DataConvertor;
import net.librec.math.algorithm.Randoms;
import net.librec.math.structure.SparseMatrix;
import net.librec.util.Lists;
import net.librec.util.RatingContext;
<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
<<<<<<< HEAD
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
=======
>>>>>>> master
=======
>>>>>>> last minute commit.

/**
 * TopN Data Splitter
 *
 * @author WangYuFeng and liuxz
 */
public class GivenNDataSplitter extends AbstractDataSplitter {
<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
<<<<<<< HEAD
    private SparseMatrix preferenceMatrix;
    private SparseMatrix datetimeMatrix;
    private static final Log LOG = LogFactory.getLog(GivenNDataSplitter.class);

    public GivenNDataSplitter() {
    }

    public GivenNDataSplitter(DataConvertor dataConvertor, Configuration conf) {
        this.dataConvertor = dataConvertor;
        this.conf = conf;
    }

    /**
     * Split ratings into two parts: the training set consisting of user-item
     * ratings where {@code numGiven} ratings are preserved for each user, and
     * the rest are used as the testing data
     *
     */
    public void getGivenNByUser(int numGiven) throws Exception {
        if (numGiven > 0) {

            trainMatrix = new SparseMatrix(preferenceMatrix);
            testMatrix = new SparseMatrix(preferenceMatrix);

            for (int u = 0, um = preferenceMatrix.numRows(); u < um; u++) {
                List<Integer> items = preferenceMatrix.getColumns(u);
                int numRated = items.size();

                if (numRated > numGiven) {
                    int[] givenIndex = Randoms.nextIntArray(numGiven, numRated);

                    for (int i = 0, j = 0; j < numRated; j++) {
                        if (i < givenIndex.length && givenIndex[i] == j) {
                            testMatrix.set(u, items.get(j), 0.0);
                            i++;
                        } else {
                            trainMatrix.set(u, items.get(j), 0.0);
                        }
                    }
                } else {
                    for (int j : items)
                        testMatrix.set(u, j, 0.0);
                }
            }
            SparseMatrix.reshape(trainMatrix);
            SparseMatrix.reshape(testMatrix);
        }
    }

    /**
     * Split ratings into two parts: the training set consisting of user-item
     * ratings where {@code numGiven} earliest ratings are preserved for each
     * user, and the rest are used as the testing data
     *
     */
    public void getGivenNByUserDate(int numGiven) {
        if (numGiven > 0) {

            trainMatrix = new SparseMatrix(preferenceMatrix);
            testMatrix = new SparseMatrix(preferenceMatrix);

            for (int u = 0, um = preferenceMatrix.numRows(); u < um; u++) {
                List<Integer> items = preferenceMatrix.getColumns(u);
                List<RatingContext> rcs = new ArrayList<>(Lists.initSize(items.size()));
                for (int j : items)
                    rcs.add(new RatingContext(u, j, (long) datetimeMatrix.get(u, j)));
                Collections.sort(rcs);

                for (int i = 0; i < rcs.size(); i++) {
                    RatingContext rc = rcs.get(i);
                    int j = rc.getItem();

                    if (i < numGiven)
                        testMatrix.set(u, j, 0.0);
                    else
                        trainMatrix.set(u, j, 0.0);
                }
            }
            SparseMatrix.reshape(trainMatrix);
            SparseMatrix.reshape(testMatrix);
        }
    }

    /**
     * Split ratings into two parts: the training set consisting of user-item
     * ratings where {@code numGiven} ratings are preserved for each item, and
     * the rest are used as the testing data
     *
     */
    public void getGivenNByItem(int numGiven) throws Exception {
        if (numGiven > 0) {

            trainMatrix = new SparseMatrix(preferenceMatrix);
            testMatrix = new SparseMatrix(preferenceMatrix);

            for (int j = 0, jm = preferenceMatrix.numColumns(); j < jm; j++) {
                List<Integer> users = preferenceMatrix.getRows(j);
                int numRated = users.size();
                if (numRated > numGiven) {

                    int[] givenIndex = Randoms.nextIntArray(numGiven, numRated);
                    for (int i = 0, k = 0; k < numRated; k++) {
                        if (i < givenIndex.length && givenIndex[i] == k) {
                            testMatrix.set(users.get(k), j, 0.0);
                            i++;
                        } else {
                            trainMatrix.set(users.get(k), j, 0.0);
                        }
                    }
                } else {
                    for (int u : users)
                        testMatrix.set(u, j, 0.0);
                }
            }
            SparseMatrix.reshape(trainMatrix);
            SparseMatrix.reshape(testMatrix);
        }
    }

    /**
     * Split ratings into two parts: the training set consisting of user-item
     * ratings where {@code numGiven} earliest ratings are preserved for each
     * item, and the rest are used as the testing data
     *
     */
    public void getGivenNByItemDate(int numGiven) {
        if (numGiven > 0) {
            trainMatrix = new SparseMatrix(preferenceMatrix);
            testMatrix = new SparseMatrix(preferenceMatrix);

            for (int j = 0, jm = preferenceMatrix.numRows(); j < jm; j++) {
                List<Integer> users = preferenceMatrix.getRows(j);
                List<RatingContext> rcs = new ArrayList<>(Lists.initSize(users.size()));

                for (int u : users)
                    rcs.add(new RatingContext(u, j, (long) datetimeMatrix.get(u, j)));

                Collections.sort(rcs);
                for (int i = 0; i < rcs.size(); i++) {
                    RatingContext rc = rcs.get(i);
                    int u = rc.getUser();

                    if (i < numGiven)
                        testMatrix.set(u, j, 0.0);
                    else
                        trainMatrix.set(u, j, 0.0);
                }
                SparseMatrix.reshape(trainMatrix);
                SparseMatrix.reshape(testMatrix);
            }
        }
    }

    @Override
    public void splitData() throws LibrecException {
        this.preferenceMatrix = dataConvertor.getPreferenceMatrix();
        this.datetimeMatrix = dataConvertor.getDatetimeMatrix();
        String splitter = conf.get("data.splitter.givenn");
        switch (splitter.toLowerCase()) {
            case "getgivennbyuser": {
                try {
                    getGivenNByUser(Integer.parseInt(conf.get("data.splitter.givenn.n")));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            }
            case "getgivennbyitem": {
                try {
                    getGivenNByItem(Integer.parseInt(conf.get("data.splitter.givenn.n")));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            }
            case "getgivennbyuserdate": {
                try {
                    getGivenNByUserDate(Integer.parseInt(conf.get("data.splitter.givenn.n")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case "getgivennbyitemdate": {
                try {
                    getGivenNByItemDate(Integer.parseInt(conf.get("data.splitter.givenn.n")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
=======
=======
>>>>>>> last minute commit.
	private SparseMatrix preferenceMatrix;
	private SparseMatrix datetimeMatrix;
	private static final Log LOG = LogFactory.getLog(GivenNDataSplitter.class);

	public GivenNDataSplitter(DataConvertor dataConvertor, Configuration conf) {
		this.dataConvertor = dataConvertor;
		this.conf = conf;
	}

	/**
	 * Split ratings into two parts: the training set consisting of user-item
	 * ratings where {@code numGiven} ratings are preserved for each user, and
	 * the rest are used as the testing data
	 *
	 */
	public void getGivenNByUser(int numGiven) throws Exception {
		if (numGiven > 0) {

			trainMatrix = new SparseMatrix(preferenceMatrix);
			testMatrix = new SparseMatrix(preferenceMatrix);

			for (int u = 0, um = preferenceMatrix.numRows(); u < um; u++) {
				List<Integer> items = preferenceMatrix.getColumns(u);
				int numRated = items.size();

				if (numRated > numGiven) {
					int[] givenIndex = Randoms.nextIntArray(numGiven, numRated);

					for (int i = 0, j = 0; j < numRated; j++) {
						if (i < givenIndex.length && givenIndex[i] == j) {
							testMatrix.set(u, items.get(j), 0.0);
							i++;
						} else {
							trainMatrix.set(u, items.get(j), 0.0);
						}
					}
				} else {
					for (int j : items)
						testMatrix.set(u, j, 0.0);
				}
			}
			SparseMatrix.reshape(trainMatrix);
			SparseMatrix.reshape(testMatrix);
		}
	}

	/**
	 * Split ratings into two parts: the training set consisting of user-item
	 * ratings where {@code numGiven} earliest ratings are preserved for each
	 * user, and the rest are used as the testing data
	 *
	 */
	public void getGivenNByUserDate(int numGiven) {
		if (numGiven > 0) {

			trainMatrix = new SparseMatrix(preferenceMatrix);
			testMatrix = new SparseMatrix(preferenceMatrix);

			for (int u = 0, um = preferenceMatrix.numRows(); u < um; u++) {
				List<Integer> items = preferenceMatrix.getColumns(u);
				List<RatingContext> rcs = new ArrayList<>(Lists.initSize(items.size()));
				for (int j : items)
					rcs.add(new RatingContext(u, j, (long) datetimeMatrix.get(u, j)));
				Collections.sort(rcs);

				for (int i = 0; i < rcs.size(); i++) {
					RatingContext rc = rcs.get(i);
					int j = rc.getItem();

					if (i < numGiven)
						testMatrix.set(u, j, 0.0);
					else
						trainMatrix.set(u, j, 0.0);
				}
			}
			SparseMatrix.reshape(trainMatrix);
			SparseMatrix.reshape(testMatrix);
		}
	}

	/**
	 * Split ratings into two parts: the training set consisting of user-item
	 * ratings where {@code numGiven} ratings are preserved for each item, and
	 * the rest are used as the testing data
	 *
	 */
	public void getGivenNByItem(int numGiven) throws Exception {
		if (numGiven > 0) {

			trainMatrix = new SparseMatrix(preferenceMatrix);
			testMatrix = new SparseMatrix(preferenceMatrix);

			for (int j = 0, jm = preferenceMatrix.numColumns(); j < jm; j++) {
				List<Integer> users = preferenceMatrix.getRows(j);
				int numRated = users.size();
				if (numRated > numGiven) {

					int[] givenIndex = Randoms.nextIntArray(numGiven, numRated);
					for (int i = 0, k = 0; k < numRated; k++) {
						if (i < givenIndex.length && givenIndex[i] == k) {
							testMatrix.set(users.get(k), j, 0.0);
							i++;
						} else {
							trainMatrix.set(users.get(k), j, 0.0);
						}
					}
				} else {
					for (int u : users)
						testMatrix.set(u, j, 0.0);
				}
			}
			SparseMatrix.reshape(trainMatrix);
			SparseMatrix.reshape(testMatrix);
		}
	}

	/**
	 * Split ratings into two parts: the training set consisting of user-item
	 * ratings where {@code numGiven} earliest ratings are preserved for each
	 * item, and the rest are used as the testing data
	 *
	 */
	public void getGivenNByItemDate(int numGiven) {
		if (numGiven > 0) {
			trainMatrix = new SparseMatrix(preferenceMatrix);
			testMatrix = new SparseMatrix(preferenceMatrix);

			for (int j = 0, jm = preferenceMatrix.numRows(); j < jm; j++) {
				List<Integer> users = preferenceMatrix.getRows(j);
				List<RatingContext> rcs = new ArrayList<>(Lists.initSize(users.size()));

				for (int u : users)
					rcs.add(new RatingContext(u, j, (long) datetimeMatrix.get(u, j)));

				Collections.sort(rcs);
				for (int i = 0; i < rcs.size(); i++) {
					RatingContext rc = rcs.get(i);
					int u = rc.getUser();

					if (i < numGiven)
						testMatrix.set(u, j, 0.0);
					else
						trainMatrix.set(u, j, 0.0);
				}
				SparseMatrix.reshape(trainMatrix);
				SparseMatrix.reshape(testMatrix);
			}
		}
	}

	@Override
	public void splitData() throws LibrecException {
		this.preferenceMatrix = dataConvertor.getPreferenceMatrix();
		this.datetimeMatrix = dataConvertor.getDatetimeMatrix();
		String splitter = conf.get("givenn.data.splitter");
		switch (splitter.toLowerCase()) {
		case "getgivennbyuser": {
			try {
				getGivenNByUser(Integer.parseInt(conf.get("data.splitter.given.n")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		case "getgivennbyitem": {
			try {
				getGivenNByItem(Integer.parseInt(conf.get("data.splitter.given.n")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		case "getgivennbyuserdate": {
			try {
				getGivenNByUserDate(Integer.parseInt(conf.get("data.splitter.given.n")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case "getgivennbyitemdate": {
			try {
				getGivenNByItemDate(Integer.parseInt(conf.get("data.splitter.given.n")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		}
	}
<<<<<<< refs/remotes/2.0.0-beta/2.0.0-beta
>>>>>>> master
=======
>>>>>>> last minute commit.
}