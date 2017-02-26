%{
	String[] pieces = _arg.split("\\.");
	Object obj = _caller.get(pieces[0]);
	this.setProperty(pieces[0], obj);

	if(_type == null) {
		_type = "text";
	}
}%

#{field _arg}
<div class="control-group#{ifError field.name} error#{/ifError}">
	<!--<label class="control-label" for="${field.id}">&{field.name}</label>-->
	<div class="control">
	#{if _type == 'select'}
		#{if _multiple}
			#{select items:_items, labelProperty:'name', name:field.name, id:field.name, value:field.value, multiple:'',class:'form-control3'}
				#{doBody /}
			#{/select}
		#{/if}
		#{else}
			#{select items:_items, labelProperty:_labelProperty, name:field.name, id:field.name, value:field.value,class:'form-control'}
				#{doBody /}
			#{/select}
		#{/else}
	#{/if}
	#{else}
		<input class="form-control" #{if _accept}accept="${_accept}" #{/if}id="${field.id}" name="${field.name}"  type="${_type}" value="${field.value?.raw()?.escape()}">
		
		*{#{html5.input for:field.name, id:field.id, type:_type, placeholder:_placeholder,class:'form-control' /}}*
	#{/else}
	<span class="help-inline error_color">#{ifError field.name}${field.error.raw()}#{/ifError}#{else}${_hint}#{/else}</span>
	</div>
</div>
#{/field}
