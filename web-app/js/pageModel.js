/**
 * Created with IntelliJ IDEA.
 * User: jzhong
 * Date: 5/17/13
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */

function CreatePageComponent(params){
    this.type=params.type;
    this.name=params.name;
    this.title=params.title;
    this.scriptingLanguage=params.scriptingLanguage;
    this.label=params.label;
    this.placeholder=params.placeholder;
    this.model=params.model;
    this.parameters=params.parameters;
    this.sourceModel=params.sourceModel;
    this.sourceValue = [];
    this.sourceParameters = params.sourceParameters;
    this.sequence = params.sequence;
    this.activated = params.activated;
    this.nextButtonLabel = params.nextButtonLabel;
    this.lastButtonLabel = params.lastButtonLabel;
    this.submitLabel = params.submitLabel;
    this.onLoad = params.onLoad;
    this.loadInitially = params.loadInitially;
    this.value = params.value;
    this.required = params.required;
    this.readonly = params.readonly;
    this.onClick = params.onClick;
    this.showInitially = params.showInitially;
    this.visible = params.visible;
    this.submit = params.submit;
    this.allowNew = params.allowNew;
    this.allowModify = params.allowModify;
    this.allowDelete = params.allowDelete;
    this.allowReload = params.allowReload;
    this.pageSize = params.pageSize;
    this.labelKey = params.labelKey;
    this.valueKey = params.valueKey;
    this.booleanTrueValue = params.booleanTrueValue;
    this.booleanFalseValue = params.booleanFalseValue;
    this.description = params.description;
    this.url = params.url;
    this.imageUrl = params.imageUrl;
    this.style = params.style;
    this.resource = params.resource;
    this.binding = params.binding;
    this.validation = params.validation;
    this.onUpdate = params.onUpdate;
    this.documentation = params.documentation;
    this.components = params.components;

    this.get = function () {

    }
}
