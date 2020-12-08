package com.storyhasyou.kratos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Translation response dto.
 *
 * @author fangxi
 */
@Data
public class TranslationResponseDTO implements Serializable {

    /**
     * The Query.
     */
    private String query;
    /**
     * The Error code.
     */
    private String errorCode;
    /**
     * The L.
     */
    private String l;
    /**
     * The T speak url.
     */
    @JsonProperty("tSpeakUrl")
    private String tSpeakUrl;
    /**
     * The Request id.
     */
    private String requestId;
    /**
     * The Dict.
     */
    private Dict dict;
    /**
     * The Webdict.
     */
    private Webdict webdict;
    /**
     * The Basic.
     */
    private Basic basic;
    /**
     * The Is word.
     */
    private Boolean isWord;
    /**
     * The Speak url.
     */
    private String speakUrl;
    /**
     * The Return phrase.
     */
    private List<String> returnPhrase;
    /**
     * The Web.
     */
    private List<Web> web;
    /**
     * The Translation.
     */
    private List<String> translation;

    /**
     * The type Dict.
     */
    @NoArgsConstructor
    @Data
    public static class Dict {
        /**
         * The Url.
         */
        private String url;
    }

    /**
     * The type Webdict.
     */
    @NoArgsConstructor
    @Data
    public static class Webdict {
        /**
         * The Url.
         */
        private String url;
    }

    /**
     * The type Basic.
     */
    @NoArgsConstructor
    @Data
    public static class Basic {
        /**
         * The Usphonetic.
         */
        @JsonProperty("us-phonetic")
        private String usphonetic;
        /**
         * The Phonetic.
         */
        private String phonetic;
        /**
         * The Ukphonetic.
         */
        @JsonProperty("uk-phonetic")
        private String ukphonetic;
        /**
         * The Ukspeech.
         */
        @JsonProperty("uk-speech")
        private String ukspeech;
        /**
         * The Usspeech.
         */
        @JsonProperty("us-speech")
        private String usspeech;
        /**
         * The Exam type.
         */
        @JsonProperty("exam_type")
        private List<String> examType;
        /**
         * The Wfs.
         */
        private List<WfsDTO> wfs;
        /**
         * The Explains.
         */
        private List<String> explains;

    }

    /**
     * The type Wfs dto.
     */
    @NoArgsConstructor
    @Data
    public static class WfsDTO {

        /**
         * wf : {"name":"复数","value":"hellos"}
         */
        private WfDTO wf;

        /**
         * The type Wf dto.
         */
        @NoArgsConstructor
        @Data
        public static class WfDTO {

            /**
             * name : 复数
             * value : hellos
             */
            private String name;
            /**
             * The Value.
             */
            private String value;
        }
    }

    /**
     * The type Web.
     */
    @NoArgsConstructor
    @Data
    public static class Web {
        /**
         * The Key.
         */
        private String key;
        /**
         * The Value.
         */
        private List<String> value;
    }


}
