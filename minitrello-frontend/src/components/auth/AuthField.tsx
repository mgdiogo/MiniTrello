type AuthFieldProps = {
    icon?: React.ComponentType<{className?: string}>,
    fieldLabel: string,
    fieldType: string,
    fieldPlaceholder: string,
    value: string,
    onChange: (value: string) => void
    extra?: React.ReactNode
}

export default function AuthField({
    icon: Icon,
    fieldLabel,
    fieldType,
    fieldPlaceholder,
    value,
    onChange,
    extra
}: AuthFieldProps) {
    return (
        <>
            <div className="field-group">
                { extra ? (
                    <div className="field-label-row">
                        <label className="field-label">{fieldLabel}</label>
                        {extra}
                    </div>
                ) : (
                <label className="field-label">{fieldLabel}</label>
                )}
                <div className="field-input-wrapper">
                    {Icon ? <Icon className="field-icon"/> : null}
                    <input className={Icon ? "field-input" : "field-input-no-icon"} type={fieldType} placeholder={fieldPlaceholder} value={value} onChange={(e) => onChange(e.target.value)} required />
                </div>
            </div>
        </>
    )
}