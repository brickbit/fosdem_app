package com.rgr.fosdem.android.provider

import android.content.Context
import com.rgr.fosdem.android.R
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.model.VersionBo
import com.rgr.fosdem.domain.repository.JsonProvider
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter
import java.io.Writer


class JsonProviderImpl(
    private val context: Context
): JsonProvider {

    override suspend fun getSchedule(): Result<List<TrackBo>> {
        val stream: InputStream = context.resources.openRawResource(R.raw.content)
        val writer: Writer = StringWriter()
        val buffer = CharArray(1024)
        stream.use { stream ->
            val reader: Reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        }

        val jsonString: String = writer.toString()
        val model = Json.decodeFromString<List<TrackBo>>(jsonString)
        return Result.success(model)
    }

    override suspend fun getVersion(): Result<VersionBo> {
        return Result.success(VersionBo("0.0.1",""))
    }

}