package entry;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author: MaoMao
 * @date: 2023/5/5 22:02
 */
public class IdxInformation {

    //@ApiModelProperty(value = "唯一id")
    private String uuid;

    //@ApiModelProperty(value = "数据排序")
    private Long order;

    //@ApiModelProperty(value = "数据类型")
    private Integer dataType;

    //@ApiModelProperty(value = "数据类型")
    private String dataTypeStr;

    //@ApiModelProperty(value = "发表年份")
    private Integer publishYear;

    //@ApiModelProperty(value = "创建时间")
    private LocalDate createTime;

    //@ApiModelProperty(value = "更新时间")
    private LocalDate updateTime;

    //@ApiModelProperty(value = "网站类型")
    private String webType;

    //@ApiModelProperty(value = "数据导入类型，0爬虫，1人工")
    private Integer importType;

    //@ApiModelProperty(value = "动态id")
    private String dynamicId;

    //@ApiModelProperty(value = "标题(原文)")
    private String title;

    //@ApiModelProperty(value = "标题(中文)")
    private String titleCn;

    //@ApiModelProperty(value = "作者")
    private List<String> authors;

    //@ApiModelProperty(value = "编辑")
    private String editors;

    //@ApiModelProperty(value = "摘要（原文）")
    private String abstracts;

    //@ApiModelProperty(value = "摘要（中文）")
    private String abstractsCn;

    //@ApiModelProperty(value = "关键词")
    private List<String> keywords;

    //@ApiModelProperty(value = "附加关键词")
    private List<String> otherKeywords;

    //@ApiModelProperty(value = "正文（原文）")
    private String content;

    //@ApiModelProperty(value = "正文（中文）")
    private String contentCn;

    //@ApiModelProperty(value = "评论")
    private String comments;

    //@ApiModelProperty(value = "图片")
    private String image;

    //@ApiModelProperty(value = "图片描述")
    private String imagesDescribe;

    //@ApiModelProperty(value = "图片路径")
    private String imagesUrl;

    //@ApiModelProperty(value = "发布时间")
    private LocalDate publishTime;

    //@ApiModelProperty(value = "采集时间")
    private LocalDate collectTime;

    //@ApiModelProperty(value = "动态网站")
    private String trends;

    //@ApiModelProperty(value = "动态板块")
    private String dynamicPlate;

    //@ApiModelProperty(value = "来源")
    private String source;

    //@ApiModelProperty(value = "动态领域")
    private List<String> dynamicDomain;

    //@ApiModelProperty(value = "网址")
    private String collectUrl;

    //@ApiModelProperty(value = "储存地址")
    private String snapshotUrl;

    //@ApiModelProperty(value = "点赞")
    private Integer praiseNum;

    //@ApiModelProperty(value = "收藏")
    private Integer collectNum;

    //@ApiModelProperty(value = "转发")
    private Integer repeatNum;

    //@ApiModelProperty(value = "阅读")
    private Integer readNum;

    //@ApiModelProperty(value = "原创")
    private String original;

    //@ApiModelProperty(value = "原文语言")
    private String language;

    //@ApiModelProperty(value = "相关人员")
    private String aboutPersonnel;

    //@ApiModelProperty(value = "相关机构")
    private String aboutOrg;

    //@ApiModelProperty(value = "相关技术")
    private String aboutTechnology;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getDataTypeStr() {
        return dataTypeStr;
    }

    public void setDataTypeStr(String dataTypeStr) {
        this.dataTypeStr = dataTypeStr;
    }

    public Integer getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public LocalDate getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDate updateTime) {
        this.updateTime = updateTime;
    }

    public String getWebType() {
        return webType;
    }

    public void setWebType(String webType) {
        this.webType = webType;
    }

    public Integer getImportType() {
        return importType;
    }

    public void setImportType(Integer importType) {
        this.importType = importType;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleCn() {
        return titleCn;
    }

    public void setTitleCn(String titleCn) {
        this.titleCn = titleCn;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getEditors() {
        return editors;
    }

    public void setEditors(String editors) {
        this.editors = editors;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getAbstractsCn() {
        return abstractsCn;
    }

    public void setAbstractsCn(String abstractsCn) {
        this.abstractsCn = abstractsCn;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getOtherKeywords() {
        return otherKeywords;
    }

    public void setOtherKeywords(List<String> otherKeywords) {
        this.otherKeywords = otherKeywords;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentCn() {
        return contentCn;
    }

    public void setContentCn(String contentCn) {
        this.contentCn = contentCn;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImagesDescribe() {
        return imagesDescribe;
    }

    public void setImagesDescribe(String imagesDescribe) {
        this.imagesDescribe = imagesDescribe;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public LocalDate getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDate publishTime) {
        this.publishTime = publishTime;
    }

    public LocalDate getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(LocalDate collectTime) {
        this.collectTime = collectTime;
    }

    public String getTrends() {
        return trends;
    }

    public void setTrends(String trends) {
        this.trends = trends;
    }

    public String getDynamicPlate() {
        return dynamicPlate;
    }

    public void setDynamicPlate(String dynamicPlate) {
        this.dynamicPlate = dynamicPlate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<String> getDynamicDomain() {
        return dynamicDomain;
    }

    public void setDynamicDomain(List<String> dynamicDomain) {
        this.dynamicDomain = dynamicDomain;
    }

    public String getCollectUrl() {
        return collectUrl;
    }

    public void setCollectUrl(String collectUrl) {
        this.collectUrl = collectUrl;
    }

    public String getSnapshotUrl() {
        return snapshotUrl;
    }

    public void setSnapshotUrl(String snapshotUrl) {
        this.snapshotUrl = snapshotUrl;
    }

    public Integer getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(Integer praiseNum) {
        this.praiseNum = praiseNum;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public Integer getRepeatNum() {
        return repeatNum;
    }

    public void setRepeatNum(Integer repeatNum) {
        this.repeatNum = repeatNum;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAboutPersonnel() {
        return aboutPersonnel;
    }

    public void setAboutPersonnel(String aboutPersonnel) {
        this.aboutPersonnel = aboutPersonnel;
    }

    public String getAboutOrg() {
        return aboutOrg;
    }

    public void setAboutOrg(String aboutOrg) {
        this.aboutOrg = aboutOrg;
    }

    public String getAboutTechnology() {
        return aboutTechnology;
    }

    public void setAboutTechnology(String aboutTechnology) {
        this.aboutTechnology = aboutTechnology;
    }
}
