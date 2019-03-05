import React from 'react';
import './Form.css';

class FormRow extends React.Component {
    render() {
        const {label, children} = this.props;
        return (
            <div className='FormRow'>
                <label>{label}</label>
                {children}
            </div>
        )
    }
}

class Form extends React.Component {
    static Row = FormRow;

    render() {
        const {onSubmit} = this.props;

        return (
            <form className='Form' onSubmit={onSubmit}>
                {this.props.children}
            </form>
        )
    }
}

export default Form;
