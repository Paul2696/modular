function configDatePicker(){
    ko.bindingHandlers.datepicker = {
        init: function(element, valueAccessor, allBindingsAccessor) {
            //initialize datepicker with some optional options
            var options = allBindingsAccessor().datepickerOptions || {};
            $(element).datepicker(options);

            //handle the field changing
            ko.utils.registerEventHandler(element, "change", function () {
                var observable = valueAccessor();
                observable($(element).datepicker("getDate"));
            });

            //handle disposal (if KO removes by the template binding)
            ko.utils.domNodeDisposal.addDisposeCallback(element, function() {
                $(element).datepicker("destroy");
            });

        },
        //update the control when the view model changes
        update: function(element, valueAccessor) {
            var value = ko.utils.unwrapObservable(valueAccessor()),
                current = $(element).datepicker("getDate");

            if (value - current !== 0) {
                $(element).datepicker("setDate", value);
            }
        }
    };

    ko.bindingHandlers.fileUpload = {
        init: function (element, valueAccessor) {
            $(element).change(function () {
                valueAccessor()(element.files[0]);
            });
        },
        update: function (element, valueAccessor) {
            if (ko.unwrap(valueAccessor()) === null) {
                $(element).wrap('<form>').closest('form').get(0).reset();
                $(element).unwrap();
            }
        }
    };
}

