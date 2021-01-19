package domain.shared.exception

class WrongConfirmationHashException : Exception("confirmation hash does not match")