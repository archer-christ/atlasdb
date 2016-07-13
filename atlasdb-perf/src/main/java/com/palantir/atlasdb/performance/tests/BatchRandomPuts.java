/**
 * Copyright 2016 Palantir Technologies
 *
 * Licensed under the BSD-3 License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.palantir.atlasdb.performance.tests;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;
import com.palantir.atlasdb.keyvalue.api.Cell;
import com.palantir.atlasdb.keyvalue.api.KeyValueService;
import com.palantir.atlasdb.keyvalue.api.TableReference;
import com.palantir.atlasdb.performance.api.PerformanceTest;
import com.palantir.atlasdb.performance.api.PerformanceTestMetadata;

/**
 * This performance test performs 150 (= 300000 key-values / 2000 batch size) batch puts each of 2000 key-values.
 *
 * Both the key and value are randomly generated byte arrays.
 *
 * @author mwakerman
 */
@PerformanceTestMetadata(name="batch-random-puts", version=1)
public class BatchRandomPuts implements PerformanceTest {

    // Constants.
    static private final int NUMBER_OF_ENTRIES = 300000;
    static private final int BATCH_SIZE = 2000;
    static private final int BYTE_ARRAY_SIZE = 100;

    static private final long SEED = 2456L;
    static private final Random rand = new Random(SEED);

    static private final String TABLE_NAME = "performance.table";
    static private final String ROW_COMPONENT = "key";
    static private final String COLUMN_NAME = "value";
    static private final byte [] COLUMN_NAME_IN_BYTES = COLUMN_NAME.getBytes();

    // Setup.
    private KeyValueService kvs;
    private TableReference tableRef;

    @Override
    public void run() {
        for (int i=0; i<NUMBER_OF_ENTRIES/BATCH_SIZE; i++) {
            Map<Cell, byte[]> map = Maps.newHashMap();
            for (int j=0; j<BATCH_SIZE; j++) {
                byte[] key = new byte[32];
                byte[] value = new byte[BYTE_ARRAY_SIZE];
                rand.nextBytes(key);
                rand.nextBytes(value);
                map.put(Cell.create(key, COLUMN_NAME_IN_BYTES), value);
            }
            kvs.put(tableRef, map, 1);
        }
    }

    @Override
    public void setup(KeyValueService kvs) {
        this.kvs = kvs;
        this.tableRef = TestUtils.createTable(kvs, TABLE_NAME, ROW_COMPONENT, COLUMN_NAME);
    }

    @Override
    public void tearDown() {
        kvs.dropTable(tableRef);
    }

}