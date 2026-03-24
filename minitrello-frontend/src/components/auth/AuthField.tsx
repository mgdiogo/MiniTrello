type AuthFieldProps = {
    icon?: React.ComponentType<{ className?: string }>,
    fieldLabel: string
    fieldType: string
    fieldPlaceholder: string
    value: string
    onChange: (value: string) => void
    extra?: React.ReactNode
    errorMessage?: string
}

export default function AuthField({
    icon: Icon,
    fieldLabel,
    fieldType,
    fieldPlaceholder,
    value,
    onChange,
    extra,
    errorMessage
}: AuthFieldProps) {
    const inputClassName = [
        Icon ? "field-input" : "field-input-no-icon",
        errorMessage ? "field-input-error" : ""
    ].join(" ").trim()
    return (
        <>
            <div className="field-group">
                {extra ? (
                    <div className="field-label-row">
                        <label className="field-label">{fieldLabel}</label>
                        {extra}
                    </div>
                ) : (
                    <label className="field-label">{fieldLabel}</label>
                )}
                <div className="field-input-wrapper">
                    {Icon ? <Icon className="field-icon" /> : null}
                    <input
                        className={inputClassName}
                        type={fieldType}
                        placeholder={fieldPlaceholder}
                        value={value}
                        onChange={(e) => onChange(e.target.value)}
                        required
                    />
                </div>
                {errorMessage && <p className="auth-error">{errorMessage}</p>}
            </div>
        </>
    )
}