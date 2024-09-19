package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.model.xml.ScheduleDtoXml
import com.rgr.fosdem.domain.repository.NetworkRepository
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.core.XmlVersion
import nl.adaptivity.xmlutil.serialization.XML

class LoadDataUseCase(
    private val networkRepository: NetworkRepository
) {

    private val xml: XML by lazy {
        XML {
            xmlVersion = XmlVersion.XML10
            xmlDeclMode = XmlDeclMode.Auto
            indentString = "  "
            repairNamespaces = true
            autoPolymorphic = true
        }
    }

    suspend operator fun invoke(): Result<String> {
        return networkRepository.loadData()
            .onSuccess {
                parseXml(it)
            }
            .onFailure {
                return Result.failure(it)
            }
    }

    private fun parseXml(apiData: String): Result<ScheduleDtoXml> {
        try {
            val schedule = xml.decodeFromString(
                deserializer = ScheduleDtoXml.serializer(),
                string = apiData
                    .replace("<br/>", "\n")
                    .replace("<abstract>","<abstract><![CDATA[")
                    .replace("</abstract>","]]></abstract>")
                    .replace("<title>","<title><![CDATA[")
                    .replace("</title>","]]></title>")
                    .replace("&", "&amp;")
            )
            return Result.success(schedule)
        } catch (e: Exception) {
            return Result.failure(ErrorType.ParseXmlError)
        }
    }
}























