/**
 * Copyright 2015 Palantir Technologies
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
 */
package com.palantir.atlasdb.sweep;

import javax.annotation.Nullable;

import com.palantir.atlasdb.keyvalue.api.SweepResults;
import com.palantir.atlasdb.keyvalue.api.TableReference;
import com.palantir.atlasdb.protos.generated.TableMetadataPersistence.SweepStrategy;

public interface SweepTaskRunner {
    /**
     * Performs a dry run of sweep, doing all of the reading, but not deleting data.
     */
    SweepResults dryRun(TableReference tableRef, int rowBatchSize, int cellBatchSize, @Nullable byte[] startRow);

    SweepResults run(TableReference tableRef, int rowBatchSize, int cellBatchSize, @Nullable byte[] startRow);

    long getSweepTimestamp(SweepStrategy sweepStrategy);
}
