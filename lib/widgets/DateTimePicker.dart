import 'dart:async';

import 'package:flutter/material.dart';
import 'package:watoplan/utils/DataUtils.dart';

class DateTimePicker extends StatefulWidget {

  DateTime when;
  final setDate;
  final setTime;
  final Color color;

  DateTimePicker({
    Key key,
    this.when,
    this.setDate,
    this.setTime,
    this.color,
  }) : super(key: key);

  @override
  State<DateTimePicker> createState() => new DateTimePickerState();

}

class DateTimePickerState extends State<DateTimePicker> {

  Future<Null> _selectDate(BuildContext context) async {
    final DateTime picked = await showDatePicker(
      context: context,
      initialDate: widget.when,
      firstDate: new DateTime(2015),
      lastDate: new DateTime(2100),
    );
    if (picked != null && picked != widget.when)
      setState(() {
        widget.when = widget.setDate(picked);
      });
  }

  Future<Null> _selectTime(BuildContext context) async {
    final TimeOfDay picked = await showTimePicker(
      context: context,
      initialTime: new TimeOfDay.fromDateTime(widget.when),
    );
    if (picked != null && picked != new TimeOfDay.fromDateTime(widget.when))
      setState(() {
        widget.when = widget.setTime(picked);
      });
  }

  @override
  Widget build(BuildContext context) {
    final TextStyle valueStyle = Theme.of(context).textTheme.title;

    return new Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: <Widget>[
        new MaterialButton(
          padding: new EdgeInsets.all(12.0),
          child: new Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: <Widget>[
              new Text(
                TimeOfDay.fromDateTime(widget.when).format(context),
                style: valueStyle,
              ),
              new Icon(Icons.arrow_drop_down, color: widget.color),
            ],
          ),
          onPressed: () { _selectTime(context); },
        ),
        new MaterialButton(
          padding: new EdgeInsets.all(12.0),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: <Widget>[
              new Text(
                DateTimeUtils.formatDMY(widget.when),
                style: valueStyle,
              ),
              new Icon(Icons.arrow_drop_down, color: widget.color),
            ],
          ),
          onPressed: () { _selectDate(context); },
        ),
      ],
    );
  }

}
