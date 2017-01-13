//split IE to make sure 
function breakCSS(less_parser, css_input, callback) {
 if(!less_parser) {
	callback.call(null, false);
	return;
 }
 
 less_parser.parse(css_input, function(e, tree) {
	if(e) {
		callback.call(null, false);
		console.log($e);
		return;
	}

	var rules = tree.rules;
	var totalSelectors = 0;
	var lastIndex = -1;//the index to split CSS files from
	
	var ie_limit = 4090;//4095;

	for(var i = 0, l = rules.length; i < l ; i++) {
		var rule = rules[i];
		if(rule.type == "Ruleset") {
			totalSelectors += rule.selectors.length;
			if(totalSelectors > ie_limit) break;
			lastIndex = rule.selectors[0].elements[0].index;
		}
		else if(rule.type == "Media") {
			var $rules = rule.rules[0].rules;
			for(var k = 0 ; k < $rules.length; k++) {
				if($rules[k].type == "Ruleset") {
					totalSelectors += $rules[k].selectors.length;
				}
			}
		}
		if(totalSelectors > ie_limit) break;
	}
	
	
	var css_part2 = false;
	if(totalSelectors > ie_limit) {
		css_part2 = css_input.substring(lastIndex , css_input.length);
	}
	
	callback.call(null, css_part2);
 })
}



//this is used for grunt
if(typeof module !== 'undefined') {
 var fs = require('fs');
 var vm = require('vm');
 var less = require('less');
 less_parser = new(less.Parser)({
    processImports: false
 });

 module.exports = function fixIE(grunt) {
	var css_file = fs.readFileSync(__dirname+'/../../assets/css/ace.css' , 'utf-8');
	breakCSS(less_parser, css_file, function(css_part2) {
		if(typeof css_part2 == 'string' && css_part2.length > 10) {
			fs.writeFileSync(__dirname+'/../../assets/css/ace-part2.css' , css_part2 , 'utf-8');
			grunt.log.writeln('ace-part2.css for IE9 and below created.');
		}
	});
 }
}
